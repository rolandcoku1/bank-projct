package com.wearhouse.bankproject.service;

import com.wearhouse.bankproject.historical.entity.FactLoanPerformance;
import com.wearhouse.bankproject.historical.repository.FactLoanRepository;
import com.wearhouse.bankproject.risk.entity.PortfolioRiskMetric;
import com.wearhouse.bankproject.risk.repository.PortfolioRiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RiskAnalysisService {


    private final FactLoanRepository warehouseRepo;
    private final PortfolioRiskRepository riskMartRepo;

    public RiskAnalysisService(FactLoanRepository warehouseRepo, PortfolioRiskRepository riskMartRepo) {
        this.warehouseRepo = warehouseRepo;
        this.riskMartRepo = riskMartRepo;
    }

    @Transactional(transactionManager = "riskTransactionManager")
    public void generateDailyRiskReport() {
        System.out.println("--- STARTING RISK ANALYSIS ---");

        List<FactLoanPerformance> allFacts = warehouseRepo.findAll();

        BigDecimal totalLoans = BigDecimal.ZERO;
        BigDecimal totalRisk = BigDecimal.ZERO;

        for (FactLoanPerformance fact : allFacts) {
            if (fact.getRemainingBalance() != null) {
                totalLoans = totalLoans.add(fact.getRemainingBalance());

                BigDecimal exposure = fact.getRemainingBalance().multiply(fact.getRiskWeight());
                totalRisk = totalRisk.add(exposure);
            }
        }

        String category = "LOW";
        if (totalLoans.compareTo(BigDecimal.ZERO) > 0) {
            double ratio = totalRisk.doubleValue() / totalLoans.doubleValue();
            if (ratio > 0.20) category = "CRITICAL";
            else if (ratio > 0.10) category = "MODERATE";
        }

        PortfolioRiskMetric report = new PortfolioRiskMetric();
        report.setReportDate(LocalDate.now());
        report.setTotalOutstandingLoans(totalLoans);
        report.setTotalRiskExposure(totalRisk);
        report.setRiskCategory(category);

        riskMartRepo.save(report);

        System.out.println("--- RISK REPORT GENERATED: " + category + " ---");
    }
}