package com.wearhouse.bankproject.operational.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardStatsDTO {
    private Long totalClients;
    private Long totalAccounts;
    private Long totalLoans;
    private Long totalTransactionsToday;
    private BigDecimal totalBalance;
    private BigDecimal totalLoanAmount;
    private List<RecentTransactionDTO> recentTransactions;
    private List<AccountSummaryDTO> topAccounts;

    public DashboardStatsDTO() {}

    public Long getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(Long totalClients) {
        this.totalClients = totalClients;
    }

    public Long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(Long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public Long getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(Long totalLoans) {
        this.totalLoans = totalLoans;
    }

    public Long getTotalTransactionsToday() {
        return totalTransactionsToday;
    }

    public void setTotalTransactionsToday(Long totalTransactionsToday) {
        this.totalTransactionsToday = totalTransactionsToday;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public List<RecentTransactionDTO> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<RecentTransactionDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    public List<AccountSummaryDTO> getTopAccounts() {
        return topAccounts;
    }

    public void setTopAccounts(List<AccountSummaryDTO> topAccounts) {
        this.topAccounts = topAccounts;
    }
}
