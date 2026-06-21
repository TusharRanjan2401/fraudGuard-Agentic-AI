package com.fraudDetection.FraudGuard.controllers;

import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fraudDetection.FraudGuard.dto.FraudInvestigationReportRequest;
import com.fraudDetection.FraudGuard.entities.FraudInvestigationReport;
import com.fraudDetection.FraudGuard.services.FraudInvestigationReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FraudInvestigationController {
    private final FraudInvestigationReportService reportService;

    @PostMapping
    public ResponseEntity<FraudInvestigationReport> saveReport(@RequestBody FraudInvestigationReportRequest request){
        return ResponseEntity.ok(reportService.saveReport(request));
    }

    @GetMapping("/alert/{alertId}")
    public ResponseEntity<FraudInvestigationReport> getReportByAlertId(@PathVariable Long alertId) {
        return ResponseEntity.ok(reportService.getReportByAlertId(alertId));
    }
    
    @GetMapping("/pdf/{alertId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long alertId) throws MalformedURLException {
         System.out.println(
            "DOWNLOAD PDF CONTROLLER HIT: "
            + alertId
    );
        Resource resource = reportService.getPdfByAlertId(alertId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
