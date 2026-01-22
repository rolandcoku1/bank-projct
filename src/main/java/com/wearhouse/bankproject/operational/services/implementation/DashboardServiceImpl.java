package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.dto.AccountSummaryDTO;
import com.wearhouse.bankproject.operational.dto.DashboardStatsDTO;
import com.wearhouse.bankproject.operational.dto.RecentTransactionDTO;
import com.wearhouse.bankproject.operational.entity.Transaction;
import com.wearhouse.bankproject.operational.repository.AccountRepository;
import com.wearhouse.bankproject.operational.repository.ClientRepository;
import com.wearhouse.bankproject.operational.repository.LoanRepository;
import com.wearhouse.bankproject.operational.repository.TransactionRepository;
import com.wearhouse.bankproject.operational.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalClients(clientRepository.count());
        stats.setTotalAccounts(accountRepository.count());
        stats.setTotalLoans(loanRepository.count());

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<Transaction> todayTransactions = transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);
        stats.setTotalTransactionsToday((long) todayTransactions.size());

        stats.setTotalBalance(accountRepository.findAll().stream()
                .map(a -> a.getCurrentBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        stats.setTotalLoanAmount(loanRepository.findAll().stream()
                .map(l -> l.getLoanAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        List<Transaction> recentTx = transactionRepository.findAll().stream()
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .limit(10)
                .collect(Collectors.toList());

        List<RecentTransactionDTO> recentTransactionDTOs = recentTx.stream()
                .map(t -> new RecentTransactionDTO(
                        t.getTransactionId(),
                        t.getAccount().getClient().getFullName(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getTransactionDate()
                ))
                .collect(Collectors.toList());
        stats.setRecentTransactions(recentTransactionDTOs);

        List<AccountSummaryDTO> topAccounts = accountRepository.findAll().stream()
                .sorted((a1, a2) -> a2.getCurrentBalance().compareTo(a1.getCurrentBalance()))
                .limit(5)
                .map(a -> new AccountSummaryDTO(
                        a.getAccountId(),
                        a.getClient().getFullName(),
                        a.getAccountType(),
                        a.getCurrentBalance()
                ))
                .collect(Collectors.toList());
        stats.setTopAccounts(topAccounts);

        return stats;
    }

    @Override
    public DashboardStatsDTO getAdminDashboard() {
        return getDashboardStats(); // Admin sees everything
    }

    @Override
    public DashboardStatsDTO getManagerDashboard() {
        return getDashboardStats(); // Manager sees everything
    }

    @Override
    public DashboardStatsDTO getTellerDashboard() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<Transaction> todayTransactions = transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);

        stats.setTotalTransactionsToday((long) todayTransactions.size());
        stats.setTotalAccounts(accountRepository.count());

        // Recent transactions
        List<RecentTransactionDTO> recentTransactionDTOs = todayTransactions.stream()
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .limit(10)
                .map(t -> new RecentTransactionDTO(
                        t.getTransactionId(),
                        t.getAccount().getClient().getFullName(),
                        t.getTransactionType(),
                        t.getAmount(),
                        t.getTransactionDate()
                ))
                .collect(Collectors.toList());
        stats.setRecentTransactions(recentTransactionDTOs);

        return stats;
    }
}
