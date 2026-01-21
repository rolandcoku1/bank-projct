package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
public class AccountResponseDTO {
    private Integer accountId;
    private Integer clientId;
    private String clientName;
    private String accountType;
    private BigDecimal currentBalance;
    private String status;

    public AccountResponseDTO() {}

    public AccountResponseDTO(Integer accountId, Integer clientId, String clientName,
                              String accountType, BigDecimal currentBalance, String status) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.status = status;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
