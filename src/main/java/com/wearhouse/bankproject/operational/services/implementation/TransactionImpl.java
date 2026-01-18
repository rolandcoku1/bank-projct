package com.wearhouse.bankproject.operational.services.implementation;
import com.wearhouse.bankproject.operational.entity.Accounts;
import com.wearhouse.bankproject.operational.entity.Transaction;
import com.wearhouse.bankproject.operational.entity.User;
import com.wearhouse.bankproject.operational.repository.AccountRepository;
import com.wearhouse.bankproject.operational.repository.TransactionRepository;
import com.wearhouse.bankproject.operational.repository.UserRepository;
import com.wearhouse.bankproject.operational.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class TransactionImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccountId(Integer accountId) {
        return transactionRepository.findByAccountAccountId(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    @Override
    public Transaction deposit(Integer accountId, BigDecimal amount, Integer userId, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setTransactionType("deposit");
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction withdraw(Integer accountId, BigDecimal amount, Integer userId, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }

        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setCurrentBalance(account.getCurrentBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setTransactionType("withdrawal");
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction transfer(Integer fromAccountId, Integer toAccountId, BigDecimal amount, Integer userId, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        Accounts fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Accounts toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (fromAccount.getCurrentBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance in source account");
        }

        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(amount));
        accountRepository.save(fromAccount);

        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(amount));
        accountRepository.save(toAccount);

        Transaction withdrawalTx = new Transaction();
        withdrawalTx.setAccount(fromAccount);
        withdrawalTx.setUser(user);
        withdrawalTx.setAmount(amount);
        withdrawalTx.setTransactionType("transfer_out");
        withdrawalTx.setDescription("Transfer to account " + toAccountId + ": " + description);
        withdrawalTx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(withdrawalTx);

        Transaction depositTx = new Transaction();
        depositTx.setAccount(toAccount);
        depositTx.setUser(user);
        depositTx.setAmount(amount);
        depositTx.setTransactionType("transfer_in");
        depositTx.setDescription("Transfer from account " + fromAccountId + ": " + description);
        depositTx.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(depositTx);
    }
}