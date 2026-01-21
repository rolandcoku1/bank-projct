package com.wearhouse.bankproject.risk.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio_risk_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRiskMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "total_outstanding_loans")
    private BigDecimal totalOutstandingLoans;

    @Column(name = "total_risk_exposure")
    private BigDecimal totalRiskExposure;

    @Column(name = "risk_category")
    private String riskCategory; // LOW, MODERATE, CRITICAL
}