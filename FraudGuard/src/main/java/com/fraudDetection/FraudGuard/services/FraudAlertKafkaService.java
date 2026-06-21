package com.fraudDetection.FraudGuard.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudDetection.FraudGuard.FraudRuleEngine.FraudRule;
import com.fraudDetection.FraudGuard.dto.EnrichedTransactionDto;
import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;
import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.entities.type.TransactionStatus;
import com.fraudDetection.FraudGuard.repositories.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudAlertKafkaService {

    private final KafkaTemplate<String, EnrichedTransactionDto> kafkaTemplate;

    private final TransactionRepository transactionRepository;

    @Autowired
    public List<FraudRule> fraudRules;

    private static final String FRAUD_TOPIC = "fraud_alert_topic";

    private static final String SAFE_TOPIC = "safe_transaction_topic";

    @Transactional
  @KafkaListener(topics = "transaction_topic", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "transactionKafkaListenerFactory" )
    public void consumeTransaction(TransactionKafkaDto transactionKafkaDto){

      Transactions tx = transactionRepository.findById(transactionKafkaDto.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found in DB"));

        boolean isFraud = evaluateFraudRules(transactionKafkaDto);
        String reason = getFraudReason(transactionKafkaDto, isFraud);

        tx.setStatus(isFraud ? TransactionStatus.FLAGGED : TransactionStatus.COMPLETED);
        transactionRepository.save(tx);

        EnrichedTransactionDto enriched = new EnrichedTransactionDto(
                tx.getId(),
                tx.getSenderAccount(),
                tx.getReceiverAccount(),
                tx.getAmount(),
                isFraud ? "FRAUD" : "SAFE",
                reason
        );

        try {
            kafkaTemplate.send(isFraud ? FRAUD_TOPIC : SAFE_TOPIC, enriched);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public boolean evaluateFraudRules(TransactionKafkaDto tx) {
        for (FraudRule rule : fraudRules) {
            if (rule.evaluate(tx)) {
                return true;
            }
        }
        return false;
    }

    private String getFraudReason(TransactionKafkaDto tx, boolean isFraud) {
        if (!isFraud) return "None";

        for (FraudRule rule : fraudRules) {
            if (rule.evaluate(tx)) {
                return rule.getClass().getSimpleName();
            }
        }
        return "Unknown";
    }



}
