package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(Integer id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByAccountId(Integer accountId);
    List<Transaction> getTransactionsByUserId(Integer userId);
    List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Transaction deposit(Integer accountId, BigDecimal amount, Integer userId, String description);
    Transaction withdraw(Integer accountId, BigDecimal amount, Integer userId, String description);
    Transaction transfer(Integer fromAccountId, Integer toAccountId, BigDecimal amount, Integer userId, String description);
}