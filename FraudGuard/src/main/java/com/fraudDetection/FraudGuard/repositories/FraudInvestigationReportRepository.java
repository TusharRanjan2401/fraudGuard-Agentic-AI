package com.fraudDetection.FraudGuard.repositories;

import com.fraudDetection.FraudGuard.entities.FraudInvestigationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FraudInvestigationReportRepository extends JpaRepository<FraudInvestigationReport, Long> {
    Optional<FraudInvestigationReport> findByAlertId(Long alertId);
}
