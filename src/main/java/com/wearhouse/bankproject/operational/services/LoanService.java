package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.entity.Loans;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LoanService {
    Loans createLoan(Loans loan);
    Loans updateLoan(Integer id, Loans loan);
    void deleteLoan(Integer id);
    Optional<Loans> getLoanById(Integer id);
    List<Loans> getAllLoans();
    List<Loans> getLoansByClientId(Integer clientId);
    List<Loans> getActiveLoansByClient(Integer clientId);
    BigDecimal getTotalLoanAmountByClient(Integer clientId);
    Loans approveLoan(Integer loanId);
    Loans rejectLoan(Integer loanId);
}
