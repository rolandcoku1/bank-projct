package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class TransactionResponseDTO {
    private Integer transactionId;
    private Integer accountId;
    private String userName;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;

    // Constructors
    public TransactionResponseDTO() {}

    public TransactionResponseDTO(Integer transactionId, Integer accountId, String userName,
                                  String transactionType, BigDecimal amount,
                                  String description, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.userName = userName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}