package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Find accounts by client ID
    List<Account> findByClientClientId(Integer clientId);

    // Find accounts by type
    List<Account> findByAccountType(String accountType);

    // Find accounts by status
    List<Account> findByStatus(String status);

    // Find active accounts for a client
    @Query("SELECT a FROM Account a WHERE a.client.clientId = :clientId AND a.status = 'active'")
    List<Account> findActiveAccountsByClient(@Param("clientId") Integer clientId);

    // Find accounts with balance greater than
    List<Account> findByCurrentBalanceGreaterThan(BigDecimal balance);

    // Find account with transactions
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.transactions WHERE a.accountId = :id")
    Optional<Account> findByIdWithTransactions(@Param("id") Integer id);

    // Get total balance by client
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.client.clientId = :clientId")
    BigDecimal getTotalBalanceByClient(@Param("clientId") Integer clientId);

    // Count accounts by type
    @Query("SELECT COUNT(a) FROM Account a WHERE a.accountType = :type")
    Long countByAccountType(@Param("type") String type);
}