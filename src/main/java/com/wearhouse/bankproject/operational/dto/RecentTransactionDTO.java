package com.wearhouse.bankproject.operational.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecentTransactionDTO {
    private Integer transactionId;
    private String clientName;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public RecentTransactionDTO() {}

    public RecentTransactionDTO(Integer transactionId, String clientName,
                                String transactionType, BigDecimal amount,
                                LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.clientName = clientName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}
