package com.wearhouse.bankproject.operational.services.implementation;
import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.dto.TransactionRequestDTO;
import com.wearhouse.bankproject.operational.dto.TransactionResponseDTO;
import com.wearhouse.bankproject.operational.entity.Account;
import com.wearhouse.bankproject.operational.entity.Transaction;
import com.wearhouse.bankproject.operational.entity.User;
import com.wearhouse.bankproject.operational.repository.AccountRepository;
import com.wearhouse.bankproject.operational.repository.TransactionRepository;
import com.wearhouse.bankproject.operational.repository.UserRepository;
import com.wearhouse.bankproject.operational.services.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionImpl(TransactionRepository transactionRepository,
                           AccountRepository accountRepository,
                           UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        return MapperDTO.toTransactionResponseDTO(saved);
    }

    @Override
    public TransactionResponseDTO deposit(TransactionRequestDTO dto) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Deposit amount must be positive");

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setCurrentBalance(account.getCurrentBalance().add(dto.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType("deposit");
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        return MapperDTO.toTransactionResponseDTO(transactionRepository.save(transaction));
    }

    @Override
    public TransactionResponseDTO withdraw(TransactionRequestDTO dto) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Withdrawal amount must be positive");

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (account.getCurrentBalance().compareTo(dto.getAmount()) < 0)
            throw new RuntimeException("Insufficient balance");

        account.setCurrentBalance(account.getCurrentBalance().subtract(dto.getAmount()));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setUser(user);
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType("withdrawal");
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        return MapperDTO.toTransactionResponseDTO(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponseDTO> transfer(TransactionRequestDTO dto) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Transfer amount must be positive");

        Account fromAccount = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findById(dto.getUserId()) // përdor userId si destinationAccountId
                .orElseThrow(() -> new RuntimeException("Destination account not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (fromAccount.getCurrentBalance().compareTo(dto.getAmount()) < 0)
            throw new RuntimeException("Insufficient balance in source account");

        // Subtract from source
        fromAccount.setCurrentBalance(fromAccount.getCurrentBalance().subtract(dto.getAmount()));
        accountRepository.save(fromAccount);

        // Add to destination
        toAccount.setCurrentBalance(toAccount.getCurrentBalance().add(dto.getAmount()));
        accountRepository.save(toAccount);

        // Create withdrawal transaction
        Transaction outTx = new Transaction();
        outTx.setAccount(fromAccount);
        outTx.setUser(user);
        outTx.setAmount(dto.getAmount());
        outTx.setTransactionType("transfer_out");
        outTx.setDescription("Transfer to account " + toAccount.getAccountId() + ": " + dto.getDescription());
        outTx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(outTx);

        // Create deposit transaction
        Transaction inTx = new Transaction();
        inTx.setAccount(toAccount);
        inTx.setUser(user);
        inTx.setAmount(dto.getAmount());
        inTx.setTransactionType("transfer_in");
        inTx.setDescription("Transfer from account " + fromAccount.getAccountId() + ": " + dto.getDescription());
        inTx.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(inTx);

        return Arrays.asList(
                MapperDTO.toTransactionResponseDTO(outTx),
                MapperDTO.toTransactionResponseDTO(inTx)
        );
    }

    @Override
    public TransactionResponseDTO getTransactionById(Integer id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return MapperDTO.toTransactionResponseDTO(tx);
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(MapperDTO::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Integer accountId) {
        return transactionRepository.findByAccountAccountId(accountId).stream()
                .map(MapperDTO::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(MapperDTO::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate).stream()
                .map(MapperDTO::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }
}
