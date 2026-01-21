package com.wearhouse.bankproject.operational.dto;
import com.wearhouse.bankproject.operational.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class MapperDTO {
    public static UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
    public static User toUserEntity(UserRequestDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    public static List<UserResponseDTO> toUserResponseDTOList(List<User> users) {
        return users.stream()
                .map(MapperDTO::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public static ClientResponseDTO toClientResponseDTO(Clients client) {
        if (client == null) return null;
        return new ClientResponseDTO(
                client.getClientId(),
                client.getFullName(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getCreatedAt()
        );
    }

    public static Clients toClientEntity(ClientRequestDTO dto) {
        if (dto == null) return null;
        Clients client = new Clients();
        client.setFullName(dto.getFullName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        return client;
    }

    public static List<ClientResponseDTO> toClientResponseDTOList(List<Clients> clients) {
        return clients.stream()
                .map(MapperDTO::toClientResponseDTO)
                .collect(Collectors.toList());
    }

    public static AccountResponseDTO toAccountResponseDTO(Accounts account) {
        if (account == null) return null;
        return new AccountResponseDTO(
                account.getAccountId(),
                account.getClient().getClientId(),
                account.getClient().getFullName(),
                account.getAccountType(),
                account.getCurrentBalance(),
                account.getStatus()
        );
    }

    public static List<AccountResponseDTO> toAccountResponseDTOList(List<Accounts> accounts) {
        return accounts.stream()
                .map(MapperDTO::toAccountResponseDTO)
                .collect(Collectors.toList());
    }

    public static LoanResponseDTO toLoanResponseDTO(Loans loan) {
        if (loan == null) return null;
        return new LoanResponseDTO(
                loan.getLoanId(),
                loan.getClient().getClientId(),
                loan.getClient().getFullName(),
                loan.getLoanAmount(),
                loan.getInterestRate(),
                loan.getStartDate(),
                loan.getStatus()
        );
    }

    public static List<LoanResponseDTO> toLoanResponseDTOList(List<Loans> loans) {
        return loans.stream()
                .map(MapperDTO::toLoanResponseDTO)
                .collect(Collectors.toList());
    }

    public static TransactionResponseDTO toTransactionResponseDTO(Transaction transaction) {
        if (transaction == null) return null;
        return new TransactionResponseDTO(
                transaction.getTransactionId(),
                transaction.getAccount().getAccountId(),
                transaction.getUser().getName(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getTransactionDate()
        );
    }

    public static List<TransactionResponseDTO> toTransactionResponseDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(MapperDTO::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }
    public static Loans toLoanEntity(LoanRequestDTO dto) {
        if (dto == null) return null;
        Loans loan = new Loans();
        if (dto.getClientId() != null) {
            Clients client = new Clients();
            client.setClientId(dto.getClientId());
            loan.setClient(client);
        }
        loan.setLoanAmount(dto.getLoanAmount());
        loan.setInterestRate(dto.getInterestRate());
        return loan;
    }
    public static Accounts toAccountEntity(AccountRequestDTO dto) {
        if (dto == null) return null;
        Accounts account = new Accounts();
        account.setAccountType(dto.getAccountType());
        account.setCurrentBalance(dto.getCurrentBalance());
        account.setStatus(dto.getStatus());
        return account;
    }
}