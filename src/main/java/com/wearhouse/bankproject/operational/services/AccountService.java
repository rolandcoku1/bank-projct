package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.entity.Accounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Accounts createAccount(Accounts account);
    Accounts updateAccount(Integer id, Accounts account);
    void deleteAccount(Integer id);
    Optional<Accounts> getAccountById(Integer id);
    List<Accounts> getAllAccounts();
    List<Accounts> getAccountsByClientId(Integer clientId);
    List<Accounts> getActiveAccountsByClient(Integer clientId);
    Optional<Accounts> getAccountWithTransactions(Integer id);
    BigDecimal getTotalBalanceByClient(Integer clientId);
    Accounts updateBalance(Integer accountId, BigDecimal amount);
}