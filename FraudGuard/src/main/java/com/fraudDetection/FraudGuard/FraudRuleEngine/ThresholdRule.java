package com.fraudDetection.FraudGuard.FraudRuleEngine;

import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;
import com.fraudDetection.FraudGuard.entities.Transactions;
import org.springframework.stereotype.Component;

@Component
public class ThresholdRule implements FraudRule{

    private final double THRESHOLD = 100000.0;

    @Override
    public boolean evaluate(TransactionKafkaDto tx){
        if(tx.getAmount()==null)return false;
        return tx.getAmount() >= THRESHOLD;
    }
}
