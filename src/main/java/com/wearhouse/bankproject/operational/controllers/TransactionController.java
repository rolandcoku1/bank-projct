package com.wearhouse.bankproject.operational.controllers;

import com.wearhouse.bankproject.operational.dto.TransactionRequestDTO;
import com.wearhouse.bankproject.operational.dto.TransactionResponseDTO;
import com.wearhouse.bankproject.operational.services.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(dto));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.deposit(dto));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.withdraw(dto));
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionResponseDTO>> transfer(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.transfer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(startDate, endDate));
    }
}
