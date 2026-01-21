package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
public class TransactionRequestDTO {
    private Integer accountId;
    private Integer userId;
    private String transactionType;
    private BigDecimal amount;
    private String description;

    public TransactionRequestDTO() {}

    public TransactionRequestDTO(Integer accountId, Integer userId, String transactionType,
                                 BigDecimal amount, String description) {
        this.accountId = accountId;
        this.userId = userId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

