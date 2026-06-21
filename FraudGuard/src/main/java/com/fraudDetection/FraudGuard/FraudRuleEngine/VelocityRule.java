package com.fraudDetection.FraudGuard.FraudRuleEngine;

import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;
import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class VelocityRule implements FraudRule{

    private final TransactionRepository transactionRepository;
    private final int windowMinutes = 10;
    private final int maxTransactions = 5;

    public VelocityRule(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean evaluate(TransactionKafkaDto tx){
        String senderAccount = tx.getSenderAccount();
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(windowMinutes);
        List<Transactions> recent = transactionRepository
                .findBySenderAccountAndTimestampAfter(senderAccount,threshold);

        return recent.size() >= maxTransactions;
    }
}
