package com.fraudDetection.FraudGuard.dto;

import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.entities.type.AlertStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertsDto {
    private Long id;
    private Long transactionId;
    private String senderAccount;
    private String receiverAccount;
    private Double amount;
    private AlertStatus status;
    private LocalDateTime createdAt;

}
