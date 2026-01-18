package com.wearhouse.bankproject.operational.services.implementation;
import com.wearhouse.bankproject.operational.entity.Loans;
import com.wearhouse.bankproject.operational.repository.LoanRepository;
import com.wearhouse.bankproject.operational.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public Loans createLoan(Loans loan) {
        if (loan.getStartDate() == null) {
            loan.setStartDate(LocalDate.now());
        }
        if (loan.getStatus() == null) {
            loan.setStatus("pending");
        }
        return loanRepository.save(loan);
    }

    @Override
    public Loans updateLoan(Integer id, Loans loan) {
        Loans existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        existingLoan.setLoanAmount(loan.getLoanAmount());
        existingLoan.setInterestRate(loan.getInterestRate());
        existingLoan.setStatus(loan.getStatus());

        return loanRepository.save(existingLoan);
    }
    @Override
    public void deleteLoan(Integer id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan not found with id: " + id);
        }
        loanRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Loans> getLoanById(Integer id) {
        return loanRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> getLoansByClientId(Integer clientId) {
        return loanRepository.findByClientClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loans> getActiveLoansByClient(Integer clientId) {
        return loanRepository.findActiveLoansForClient(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalLoanAmountByClient(Integer clientId) {
        BigDecimal total = loanRepository.getTotalLoanAmountByClient(clientId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Loans approveLoan(Integer loanId) {
        Loans loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));

        loan.setStatus("approved");
        loan.setStartDate(LocalDate.now());
        return loanRepository.save(loan);
    }

    @Override
    public Loans rejectLoan(Integer loanId) {
        Loans loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + loanId));

        loan.setStatus("rejected");
        return loanRepository.save(loan);
    }
}
