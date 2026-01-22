package com.wearhouse.bankproject.operational.dto;

import java.math.BigDecimal;
public class AccountSummaryDTO {
    private Integer accountId;
    private String clientName;
    private String accountType;
    private BigDecimal balance;

    public AccountSummaryDTO() {}

    public AccountSummaryDTO(Integer accountId, String clientName,
                             String accountType, BigDecimal balance) {
        this.accountId = accountId;
        this.clientName = clientName;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
