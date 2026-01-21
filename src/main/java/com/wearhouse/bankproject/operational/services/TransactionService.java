package com.wearhouse.bankproject.operational.services;

import com.wearhouse.bankproject.operational.dto.TransactionRequestDTO;
import com.wearhouse.bankproject.operational.dto.TransactionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);
    TransactionResponseDTO deposit(TransactionRequestDTO dto);
    TransactionResponseDTO withdraw(TransactionRequestDTO dto);
    List<TransactionResponseDTO> transfer(TransactionRequestDTO dto);
    TransactionResponseDTO getTransactionById(Integer id);
    List<TransactionResponseDTO> getAllTransactions();
    List<TransactionResponseDTO> getTransactionsByAccountId(Integer accountId);
    List<TransactionResponseDTO> getTransactionsByUserId(Integer userId);
    List<TransactionResponseDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
