package com.wearhouse.bankproject.service;

import com.wearhouse.bankproject.operational.entity.Loan;
import com.wearhouse.bankproject.operational.repository.LoanRepository;
import com.wearhouse.bankproject.historical.entity.FactLoanPerformance;
import com.wearhouse.bankproject.historical.repository.FactLoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EtlService {

    private final LoanRepository sourceRepo;
    private final FactLoanRepository destinationRepo;

    public EtlService(LoanRepository sourceRepo, FactLoanRepository destinationRepo) {
        this.sourceRepo = sourceRepo;
        this.destinationRepo = destinationRepo;
    }

    @Transactional(transactionManager = "operationalTransactionManager", readOnly = true)
    public void runEtlProcess() {
        System.out.println("--- ETL STARTED ---");

        List<Loan> allLoans = sourceRepo.findAll();
        System.out.println("Extracted " + allLoans.size() + " loans.");

        saveSnapshots(allLoans);

        System.out.println("--- ETL FINISHED ---");
    }

    @Transactional(transactionManager = "historicalTransactionManager")
    public void saveSnapshots(List<Loan> loans) {
        int dateId = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        for (Loan loan : loans) {
            FactLoanPerformance fact = new FactLoanPerformance();

            fact.setDateId(dateId);
            fact.setLoanId(loan.getLoanId());

            if (loan.getClient() != null) {
                fact.setClientSk(loan.getClient().getClientId());
            }

            fact.setRemainingBalance(loan.getLoanAmount());

            // Calculate Risk (
            // DEFAULT = 1.0 (100% Risk), ACTIVE = 0.5, PAID = 0.0
            String status = loan.getStatus() != null ? loan.getStatus().toUpperCase() : "UNKNOWN";

            if ("DEFAULT".equals(status)) {
                fact.setRiskWeight(new BigDecimal("1.0"));
            } else if ("PAID".equals(status)) {
                fact.setRiskWeight(new BigDecimal("0.0"));
            } else {
                fact.setRiskWeight(new BigDecimal("0.5"));
            }

            destinationRepo.save(fact);
        }
    }
}