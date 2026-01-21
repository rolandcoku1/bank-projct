package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
public class AccountRequestDTO {
    private Integer clientId;
    private String accountType;
    private BigDecimal currentBalance;
    private String status;

    public AccountRequestDTO() {}

    public AccountRequestDTO(Integer clientId, String accountType,
                             BigDecimal currentBalance, String status) {
        this.clientId = clientId;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.status = status;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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