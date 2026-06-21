package com.fraudDetection.FraudGuard.repositories;

import com.fraudDetection.FraudGuard.dto.AlertsDto;
import com.fraudDetection.FraudGuard.entities.Alerts;
import com.fraudDetection.FraudGuard.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alerts, Long> {
    Optional<Alerts> findByTransaction(Transactions transaction);
}
