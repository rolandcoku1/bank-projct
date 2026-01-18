package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.entity.Accounts;
import com.wearhouse.bankproject.operational.repository.AccountRepository;
import com.wearhouse.bankproject.operational.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Accounts createAccount(Accounts account) {
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(BigDecimal.ZERO);
        }
        if (account.getStatus() == null) {
            account.setStatus("active");
        }
        return accountRepository.save(account);
    }

    @Override
    public Accounts updateAccount(Integer id, Accounts account) {
        Accounts existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setStatus(account.getStatus());

        return accountRepository.save(existingAccount);
    }
    @Override
    public void deleteAccount(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Accounts> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Accounts> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Accounts> getAccountsByClientId(Integer clientId) {
        return accountRepository.findByClientClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Accounts> getActiveAccountsByClient(Integer clientId) {
        return accountRepository.findActiveAccountsByClient(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Accounts> getAccountWithTransactions(Integer id) {
        return accountRepository.findByIdWithTransactions(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalBalanceByClient(Integer clientId) {
        BigDecimal total = accountRepository.getTotalBalanceByClient(clientId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Accounts updateBalance(Integer accountId, BigDecimal amount) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        BigDecimal newBalance = account.getCurrentBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setCurrentBalance(newBalance);
        return accountRepository.save(account);
    }
}
