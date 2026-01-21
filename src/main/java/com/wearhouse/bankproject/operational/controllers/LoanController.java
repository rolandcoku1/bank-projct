package com.wearhouse.bankproject.operational.controllers;

import com.wearhouse.bankproject.operational.dto.LoanRequestDTO;
import com.wearhouse.bankproject.operational.dto.LoanResponseDTO;
import com.wearhouse.bankproject.operational.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.createLoan(dto));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByClientId(@PathVariable Integer clientId) {
        return ResponseEntity.ok(loanService.getLoansByClientId(clientId));
    }

    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<LoanResponseDTO>> getActiveLoansForClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(loanService.getActiveLoansByClient(clientId));
    }

    @GetMapping("/client/{clientId}/total-amount")
    public ResponseEntity<BigDecimal> getTotalLoanAmountByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(loanService.getTotalLoanAmountByClient(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> updateLoan(@PathVariable Integer id, @RequestBody LoanRequestDTO dto) {
        return ResponseEntity.ok(loanService.updateLoan(id, dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LoanResponseDTO> approveLoan(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LoanResponseDTO> rejectLoan(@PathVariable Integer id) {
        return ResponseEntity.ok(loanService.rejectLoan(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Integer id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
