package com.wearhouse.bankproject.operational.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public class LoanResponseDTO {
    private Integer loanId;
    private Integer clientId;
    private String clientName;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private LocalDate startDate;
    private String status;
    public LoanResponseDTO() {}

    public LoanResponseDTO(Integer loanId, Integer clientId, String clientName,
                           BigDecimal loanAmount, BigDecimal interestRate,
                           LocalDate startDate, String status) {
        this.loanId = loanId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.status = status;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}