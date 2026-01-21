package com.wearhouse.bankproject.operational.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountRequestDTO {

    @NotNull(message = "Client ID is required")
    private Integer clientId;

    @NotNull(message = "Account type is required")
    private String accountType;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be positive")
    private BigDecimal currentBalance;

    private String status;

    public AccountRequestDTO() {}

    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
