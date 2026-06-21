package com.fraudDetection.FraudGuard.services;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.fraudDetection.FraudGuard.dto.FraudInvestigationReportRequest;
import com.fraudDetection.FraudGuard.entities.FraudInvestigationReport;
import com.fraudDetection.FraudGuard.repositories.FraudInvestigationReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FraudInvestigationReportService {
    private final FraudInvestigationReportRepository repository;

    public FraudInvestigationReport saveReport(FraudInvestigationReportRequest request) {
        FraudInvestigationReport report = FraudInvestigationReport
                .builder()
                .alertId(request.getAlertId())
                .transactionId(request.getTransactionId())
                .riskScore(request.getRiskScore())
                .summary(request.getSummary())
                .recommendation(request.getRecommendation())
                .createdAt(LocalDateTime.now())
                .pdfPath(request.getPdfPath())
                .build();

        return repository.save(report);
    }

    public FraudInvestigationReport getReportByAlertId(Long alertId) {
        return repository.findByAlertId(alertId).orElseThrow(() -> new RuntimeException("Alert Not Found!"));
    }
    
    public Resource getPdfByAlertId(Long alertId) throws MalformedURLException {
        FraudInvestigationReport report = repository.findByAlertId(alertId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        Path path = Paths.get(report.getPdfPath());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            throw new RuntimeException("PDF not found");
        }

        return resource;
    
    }
}
