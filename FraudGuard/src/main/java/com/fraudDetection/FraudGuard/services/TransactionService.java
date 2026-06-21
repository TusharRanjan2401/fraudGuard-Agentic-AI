package com.fraudDetection.FraudGuard.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudDetection.FraudGuard.dto.TransactionDto;
import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;
import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.entities.User;
import com.fraudDetection.FraudGuard.entities.type.TransactionStatus;
import com.fraudDetection.FraudGuard.repositories.TransactionRepository;
import com.fraudDetection.FraudGuard.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final FraudAlertKafkaService fraudAlertService;

    private final KafkaTemplate<String, TransactionKafkaDto> kafkaTemplate;

    private static final String TRANSACTION_TOPIC = "transaction_topic";

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public TransactionKafkaDto createTransaction(String senderUsername,String senderAccount, String receiverAccount, Double amount) throws ExecutionException, InterruptedException {
        User sender = userRepository.findByUsername(senderUsername);
        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        Transactions tx = Transactions.builder()
                .sender(sender)
                .senderAccount(sender.getAccountNumber())
                .receiverAccount(receiverAccount)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .status(TransactionStatus.COMPLETED)
                .build();



        TransactionKafkaDto probe = new TransactionKafkaDto(
                tx.getId(),
                tx.getSenderAccount(),
                tx.getReceiverAccount(),
                tx.getAmount(),
                tx.getTimestamp(),
                tx.getStatus().name()
        );

        boolean isFraud = fraudAlertService.evaluateFraudRules(probe);
        tx.setStatus(isFraud ? TransactionStatus.FLAGGED : TransactionStatus.COMPLETED);

        Transactions saved = transactionRepository.save(tx);

        TransactionKafkaDto dto = new TransactionKafkaDto(
                saved.getId(),
                saved.getSenderAccount(),
                saved.getReceiverAccount(),
                saved.getAmount(),
                saved.getTimestamp(),
                saved.getStatus().name()
        );

        kafkaTemplate.send(TRANSACTION_TOPIC, dto).get();

        messagingTemplate.convertAndSend(
                "/topic/transactions/" + sender.getUsername(), // sender
                dto
        );

//        messagingTemplate.convertAndSend(
//                "/topic/transactions/" + receiverAccount, // receiver
//                dto
//        );

        return dto;

    }

    public List<TransactionDto> getTransactionsBySender(String senderAccount){
        List<Transactions> transactions = transactionRepository.findBySenderAccount(senderAccount);
        if (transactions.isEmpty()) {
            throw new RuntimeException("Sender account not found!");
        }
        return transactions.stream().map(tx -> new TransactionDto(
                tx.getId(),
                tx.getSender().getId(),
                tx.getSenderAccount(),
                tx.getReceiverAccount(),
                tx.getAmount(),
                tx.getTimestamp(),
                tx.getStatus()
        )).collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionByReceiver(String receiverAccount){
        List<Transactions> transactions = transactionRepository.findByReceiverAccount(receiverAccount);
        if (transactions.isEmpty()) {
            throw new RuntimeException("Receiver account not found!");
        }
        return transactions.stream().map(tx -> new TransactionDto(
                tx.getId(),
                tx.getSender().getId(),
                tx.getSenderAccount(),
                tx.getReceiverAccount(),
                tx.getAmount(),
                tx.getTimestamp(),
                tx.getStatus()
        )).collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(Long id) {
        Transactions transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return TransactionDto.builder().id(transaction.getId()).senderId(transaction.getSender().getId())
                .senderAccount(transaction.getSenderAccount()).receiverAccount(transaction.getReceiverAccount())
                .amount(transaction.getAmount()).timestamp(transaction.getTimestamp()).status(transaction.getStatus())
                .build();
    }

    public List<TransactionDto>
getUserHistory(String accountNumber){

    return transactionRepository
            .findBySenderAccount(accountNumber)
            .stream()
            .map(transaction ->
                    TransactionDto.builder()
                            .id(transaction.getId())
                            .senderId(
                                    transaction.getSender().getId()
                            )
                            .senderAccount(
                                    transaction.getSenderAccount()
                            )
                            .receiverAccount(
                                    transaction.getReceiverAccount()
                            )
                            .amount(
                                    transaction.getAmount()
                            )
                            .timestamp(
                                    transaction.getTimestamp()
                            )
                            .status(
                                    transaction.getStatus()
                            )
                            .build()
            )
            .toList();
    }
}
