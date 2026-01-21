package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.dto.LoanRequestDTO;
import com.wearhouse.bankproject.operational.dto.LoanResponseDTO;
import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.entity.Loan;
import com.wearhouse.bankproject.operational.repository.LoanRepository;
import com.wearhouse.bankproject.operational.services.LoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO dto) {
        Loan loan = MapperDTO.toLoanEntity(dto);
        if (loan.getStartDate() == null) loan.setStartDate(LocalDate.now());
        if (loan.getStatus() == null) loan.setStatus("pending");

        Loan saved = loanRepository.save(loan);
        return MapperDTO.toLoanResponseDTO(saved);
    }

    @Override
    public LoanResponseDTO updateLoan(Integer id, LoanRequestDTO dto) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        existingLoan.setLoanAmount(dto.getLoanAmount());
        existingLoan.setInterestRate(dto.getInterestRate());
        if (dto.getClientId() != null) existingLoan.getClient().setClientId(dto.getClientId());

        Loan updated = loanRepository.save(existingLoan);
        return MapperDTO.toLoanResponseDTO(updated);
    }

    @Override
    public void deleteLoan(Integer id) {
        if (!loanRepository.existsById(id))
            throw new RuntimeException("Loan not found with id: " + id);
        loanRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponseDTO getLoanById(Integer id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));
        return MapperDTO.toLoanResponseDTO(loan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(MapperDTO::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getLoansByClientId(Integer clientId) {
        return loanRepository.findByClientClientId(clientId).stream()
                .map(MapperDTO::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getActiveLoansByClient(Integer clientId) {
        return loanRepository.findActiveLoansForClient(clientId).stream()
                .map(MapperDTO::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalLoanAmountByClient(Integer clientId) {
        BigDecimal total = loanRepository.getTotalLoanAmountByClient(clientId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public LoanResponseDTO approveLoan(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));
        loan.setStatus("approved");
        loan.setStartDate(LocalDate.now());
        return MapperDTO.toLoanResponseDTO(loanRepository.save(loan));
    }

    @Override
    public LoanResponseDTO rejectLoan(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));
        loan.setStatus("rejected");
        return MapperDTO.toLoanResponseDTO(loanRepository.save(loan));
    }
}
