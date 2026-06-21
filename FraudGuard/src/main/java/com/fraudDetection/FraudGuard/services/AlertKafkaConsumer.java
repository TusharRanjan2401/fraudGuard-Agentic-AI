package com.fraudDetection.FraudGuard.services;

import java.time.LocalDateTime;

import com.fraudDetection.FraudGuard.dto.AlertsDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fraudDetection.FraudGuard.dto.EnrichedTransactionDto;
import com.fraudDetection.FraudGuard.entities.Alerts;
import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.entities.type.AlertStatus;
import com.fraudDetection.FraudGuard.repositories.AlertRepository;
import com.fraudDetection.FraudGuard.repositories.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertKafkaConsumer {

    private final AlertRepository alertRepository;

    private final TransactionRepository transactionRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final FraudInvestigationProducer fraudInvestigationProducer;

    @Transactional
    @KafkaListener(topics = "fraud_alert_topic", groupId ="${spring.kafka.consumer.group-id}", containerFactory = "enrichedKafkaListenerFactory")
    public void consumeFraudTransaction(EnrichedTransactionDto enrichedTransactionDto){

        Transactions transactionId = transactionRepository
                .findById(enrichedTransactionDto.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Alerts alert = Alerts.builder()
                .transaction(transactionId)
                .senderAccount(enrichedTransactionDto.getSenderAccount())
                .receiverAccount(enrichedTransactionDto.getReceiverAccount())
                .amount(enrichedTransactionDto.getAmount())
                .status(enrichedTransactionDto.getStatus().equals("FRAUD")
                        ? AlertStatus.FRAUD
                        : AlertStatus.SAFE)
                .createdAt(LocalDateTime.now())
                .build();

        Alerts savedAlert = alertRepository.save(alert);

        fraudInvestigationProducer.publishInvestigationEvent(savedAlert.getId());

        messagingTemplate.convertAndSend("/topic/alerts", enrichedTransactionDto);
    }

    public AlertsDto getAlertById(Long alertId){

        Alerts alert = alertRepository.findById(alertId)
                .orElseThrow(() ->
                        new RuntimeException("Alert Not Found!"));

        return AlertsDto.builder()
                .id(alert.getId())
                .transactionId(alert.getTransaction().getId())
                .senderAccount(alert.getSenderAccount())
                .receiverAccount(alert.getReceiverAccount())
                .amount(alert.getAmount())
                .status(alert.getStatus())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
