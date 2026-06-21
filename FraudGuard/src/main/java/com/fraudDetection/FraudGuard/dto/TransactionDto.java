package com.fraudDetection.FraudGuard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fraudDetection.FraudGuard.entities.type.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    private Long id;
    private Long senderId;
    private String senderAccount;
    private String receiverAccount;
    private Double amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;

    public TransactionDto(Long id, String senderAccount, String receiverAccount, Double amount, TransactionStatus status) {
        this.id = id;
        this.senderAccount=senderAccount;
        this.receiverAccount=receiverAccount;
        this.amount=amount;
        this.status = status;
    }
}
