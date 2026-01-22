package com.wearhouse.bankproject.operational.controllers;
import com.wearhouse.bankproject.operational.dto.AccountRequestDTO;
import com.wearhouse.bankproject.operational.dto.AccountResponseDTO;
import com.wearhouse.bankproject.operational.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO dto) {
        try {
            AccountResponseDTO created = accountService.createAccount(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return new ResponseEntity<>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByClientId(@PathVariable Integer clientId) {
        return new ResponseEntity<>(accountService.getAccountsByClientId(clientId), HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<AccountResponseDTO>> getActiveAccountsByClient(@PathVariable Integer clientId) {
        return new ResponseEntity<>(accountService.getActiveAccountsByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<AccountResponseDTO> getAccountWithTransactions(@PathVariable Integer id) {
        return accountService.getAccountWithTransactions(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}/total-balance")
    public ResponseEntity<BigDecimal> getTotalBalanceByClient(@PathVariable Integer clientId) {
        BigDecimal total = accountService.getTotalBalanceByClient(clientId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(@PathVariable Integer id,
                                                            @RequestBody AccountRequestDTO dto) {
        try {
            AccountResponseDTO updated = accountService.updateAccount(id, dto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponseDTO> updateBalance(@PathVariable Integer id,
                                                            @RequestParam BigDecimal amount) {
        try {
            AccountResponseDTO updated = accountService.updateBalance(id, amount);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Integer id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
