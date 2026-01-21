package com.wearhouse.bankproject.historical.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "fact_loan_performance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactLoanPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long factId;

    @Column(name = "date_id")
    private Integer dateId; // Format: 20240121

    @Column(name = "client_sk")
    private Integer clientSk;

    @Column(name = "loan_id")
    private Integer loanId;

    @Column(name = "remaining_balance")
    private BigDecimal remainingBalance;

    @Column(name = "risk_weight")
    private BigDecimal riskWeight; // 0.0 (Safe) to 1.0 (Risk)
}