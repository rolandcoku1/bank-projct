package com.wearhouse.bankproject.operational.services;

import com.wearhouse.bankproject.operational.dto.AccountRequestDTO;
import com.wearhouse.bankproject.operational.dto.AccountResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO dto);
    AccountResponseDTO updateAccount(Integer id, AccountRequestDTO dto);
    void deleteAccount(Integer id);
    Optional<AccountResponseDTO> getAccountById(Integer id);
    List<AccountResponseDTO> getAllAccounts();
    List<AccountResponseDTO> getAccountsByClientId(Integer clientId);
    List<AccountResponseDTO> getActiveAccountsByClient(Integer clientId);
    Optional<AccountResponseDTO> getAccountWithTransactions(Integer id);
    BigDecimal getTotalBalanceByClient(Integer clientId);
    AccountResponseDTO updateBalance(Integer accountId, BigDecimal amount);
}
