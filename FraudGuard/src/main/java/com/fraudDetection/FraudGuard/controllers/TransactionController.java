package com.fraudDetection.FraudGuard.controllers;

import com.fraudDetection.FraudGuard.dto.TransactionDto;
import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;
import com.fraudDetection.FraudGuard.entities.Transactions;
import com.fraudDetection.FraudGuard.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestParam String senderUsername,
            @RequestParam String senderAccount,
            @RequestParam String receiverAccount,
            @RequestParam Double amount
    ) throws ExecutionException, InterruptedException {
        TransactionKafkaDto transaction =  transactionService.createTransaction(senderUsername,senderAccount, receiverAccount, amount);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of( "transaction", transaction));

    }

    @GetMapping("/sender/{account}")
        public ResponseEntity<List<TransactionDto>> getBySender(@PathVariable String account){
         return ResponseEntity.ok(transactionService.getTransactionsBySender(account));
    }

    @GetMapping("/receiver/{account}")
    public ResponseEntity<List<TransactionDto>> getByReceiver(@PathVariable String account){
        return ResponseEntity.ok(transactionService.getTransactionByReceiver(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionDto>> getUserHistory(@PathVariable String accountNumber){
        return ResponseEntity.ok(transactionService.getUserHistory(accountNumber));
    }


}

