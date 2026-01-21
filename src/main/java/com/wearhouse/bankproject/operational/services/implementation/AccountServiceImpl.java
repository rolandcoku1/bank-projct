package com.wearhouse.bankproject.operational.services.implementation;

import com.wearhouse.bankproject.operational.dto.AccountRequestDTO;
import com.wearhouse.bankproject.operational.dto.AccountResponseDTO;
import com.wearhouse.bankproject.operational.dto.MapperDTO;
import com.wearhouse.bankproject.operational.entity.Accounts;
import com.wearhouse.bankproject.operational.entity.Clients;
import com.wearhouse.bankproject.operational.repository.AccountRepository;
import com.wearhouse.bankproject.operational.repository.ClientRepository;
import com.wearhouse.bankproject.operational.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto) {
        Accounts account = MapperDTO.toAccountEntity(dto);

        Clients client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + dto.getClientId()));
        account.setClient(client);

        Accounts savedAccount = accountRepository.save(account);
        return MapperDTO.toAccountResponseDTO(savedAccount);
    }

    @Override
    public AccountResponseDTO updateAccount(Integer id, AccountRequestDTO dto) {
        Accounts account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        account.setAccountType(dto.getAccountType());
        account.setCurrentBalance(dto.getCurrentBalance());
        account.setStatus(dto.getStatus());

        Accounts updatedAccount = accountRepository.save(account);
        return MapperDTO.toAccountResponseDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDTO> getAccountById(Integer id) {
        return accountRepository.findById(id)
                .map(MapperDTO::toAccountResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        return MapperDTO.toAccountResponseDTOList(accountRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByClientId(Integer clientId) {
        return MapperDTO.toAccountResponseDTOList(accountRepository.findByClientClientId(clientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getActiveAccountsByClient(Integer clientId) {
        return MapperDTO.toAccountResponseDTOList(accountRepository.findActiveAccountsByClient(clientId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDTO> getAccountWithTransactions(Integer id) {
        return accountRepository.findByIdWithTransactions(id)
                .map(MapperDTO::toAccountResponseDTO);
    }

    @Override
    public BigDecimal getTotalBalanceByClient(Integer clientId) {
        BigDecimal total = accountRepository.getTotalBalanceByClient(clientId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public AccountResponseDTO updateBalance(Integer accountId, BigDecimal amount) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        Accounts updatedAccount = accountRepository.save(account);
        return MapperDTO.toAccountResponseDTO(updatedAccount);
    }
}
