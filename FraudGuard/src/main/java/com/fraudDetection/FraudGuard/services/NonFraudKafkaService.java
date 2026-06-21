package com.fraudDetection.FraudGuard.services;

import com.fraudDetection.FraudGuard.dto.EnrichedTransactionDto;
import com.fraudDetection.FraudGuard.entities.Alerts;
import com.fraudDetection.FraudGuard.entities.type.AlertStatus;
import com.fraudDetection.FraudGuard.repositories.AlertRepository;
import com.fraudDetection.FraudGuard.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service@RequiredArgsConstructor
public class NonFraudKafkaService {

    private final AlertRepository alertRepository;

    private final TransactionRepository transactionRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "safe_transaction_topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSafeTransaction(EnrichedTransactionDto enrichedTransactionDto) {

        transactionRepository.findById(enrichedTransactionDto.getTransactionId()).ifPresent(transaction -> {

            if (alertRepository.findByTransaction(transaction).isEmpty()) {
                Alerts alert = Alerts.builder()
                        .transaction(transaction)
                        .senderAccount(enrichedTransactionDto.getSenderAccount())
                        .receiverAccount(enrichedTransactionDto.getReceiverAccount())
                        .amount(enrichedTransactionDto.getAmount())
                        .status(AlertStatus.SAFE)
                        .createdAt(LocalDateTime.now())
                        .build();
                alertRepository.save(alert);
            }

            messagingTemplate.convertAndSend("/topic/nonfraud", enrichedTransactionDto);
        });
    }


}
