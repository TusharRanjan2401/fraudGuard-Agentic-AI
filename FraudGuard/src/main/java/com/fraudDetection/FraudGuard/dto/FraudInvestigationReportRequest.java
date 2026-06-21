package com.fraudDetection.FraudGuard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FraudInvestigationReportRequest {
    private Long alertId;
    private Long transactionId;
    private  Integer riskScore;
    private String summary;
    private String recommendation;
    private String pdfPath;
}
