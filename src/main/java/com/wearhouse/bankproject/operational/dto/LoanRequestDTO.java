package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
public class LoanRequestDTO {
    private Integer clientId;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;

    public LoanRequestDTO() {}

    public LoanRequestDTO(Integer clientId, BigDecimal loanAmount, BigDecimal interestRate) {
        this.clientId = clientId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
