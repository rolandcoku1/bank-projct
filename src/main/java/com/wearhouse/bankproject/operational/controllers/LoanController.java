package com.wearhouse.bankproject.operational.controllers;
import com.wearhouse.bankproject.operational.entity.Loans;
import com.wearhouse.bankproject.operational.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<Loans> createLoan(@RequestBody Loans loan) {
        try {
            Loans createdLoan = loanService.createLoan(loan);
            return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Loans>> getAllLoans() {
        try {
            List<Loans> loans = loanService.getAllLoans();
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loans> getLoanById(@PathVariable Integer id) {
        return loanService.getLoanById(id)
                .map(loan -> new ResponseEntity<>(loan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Loans>> getLoansByClientId(@PathVariable Integer clientId) {
        try {
            List<Loans> loans = loanService.getLoansByClientId(clientId);
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<Loans>> getActiveLoansForClient(@PathVariable Integer clientId) {
        try {
            List<Loans> loans = loanService.getActiveLoansByClient(clientId);
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{clientId}/total-amount")
    public ResponseEntity<BigDecimal> getTotalLoanAmountByClient(@PathVariable Integer clientId) {
        try {
            BigDecimal totalAmount = loanService.getTotalLoanAmountByClient(clientId);
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loans> updateLoan(@PathVariable Integer id, @RequestBody Loans loan) {
        try {
            Loans updatedLoan = loanService.updateLoan(id, loan);
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity approveLoan(@PathVariable Integer id) {
        try {
            Loans approvedLoan = loanService.approveLoan(id);
            return new ResponseEntity<>(approvedLoan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Loans> rejectLoan(@PathVariable Integer id) {
        try {
            Loans rejectedLoan = loanService.rejectLoan(id);
            return new ResponseEntity<>(rejectedLoan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteLoan(@PathVariable Integer id) {
        try {
            loanService.deleteLoan(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
