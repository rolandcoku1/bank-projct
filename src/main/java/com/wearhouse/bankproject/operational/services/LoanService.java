package com.wearhouse.bankproject.operational.services;

import com.wearhouse.bankproject.operational.dto.LoanRequestDTO;
import com.wearhouse.bankproject.operational.dto.LoanResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    LoanResponseDTO createLoan(LoanRequestDTO dto);
    LoanResponseDTO updateLoan(Integer id, LoanRequestDTO dto);
    void deleteLoan(Integer id);
    LoanResponseDTO getLoanById(Integer id);
    List<LoanResponseDTO> getAllLoans();
    List<LoanResponseDTO> getLoansByClientId(Integer clientId);
    List<LoanResponseDTO> getActiveLoansByClient(Integer clientId);
    BigDecimal getTotalLoanAmountByClient(Integer clientId);
    LoanResponseDTO approveLoan(Integer loanId);
    LoanResponseDTO rejectLoan(Integer loanId);
}
