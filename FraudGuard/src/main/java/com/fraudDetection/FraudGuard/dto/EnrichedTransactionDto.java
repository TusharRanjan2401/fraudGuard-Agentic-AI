package com.fraudDetection.FraudGuard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedTransactionDto {

    private Long transactionId;

    private String senderAccount;

    private String receiverAccount;

    private Double amount;

    private String status;

    private String reason;

    // public EnrichedTransactionDto(Long id, String senderAccount, String receiverAccount, Double amount, String status, String reason) {
    //     this.transactionId = id;
    //     this.senderAccount = senderAccount;
    //     this.receiverAccount = receiverAccount;
    //     this.amount = amount;
    //     this.status = status;
    //     this.reason = reason;
    // }
}
