package com.fraudDetection.FraudGuard.repositories;

import com.fraudDetection.FraudGuard.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findBySenderAccount(String senderAccount);
    List<Transactions> findByReceiverAccount(String receiverAccount);
    List<Transactions> findBySenderAccountAndTimestampAfter(String senderAccount, LocalDateTime after);
}
