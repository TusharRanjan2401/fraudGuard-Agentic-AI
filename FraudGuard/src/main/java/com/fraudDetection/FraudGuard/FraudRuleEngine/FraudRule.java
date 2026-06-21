package com.fraudDetection.FraudGuard.FraudRuleEngine;

public interface FraudRule {

    boolean evaluate(com.fraudDetection.FraudGuard.dto.TransactionKafkaDto tx);
}
