package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Integer> {

    // Find accounts by client ID
    List<Accounts> findByClientClientId(Integer clientId);

    // Find accounts by type
    List<Accounts> findByAccountType(String accountType);

    // Find accounts by status
    List<Accounts> findByStatus(String status);

    // Find active accounts for a client
    @Query("SELECT a FROM Accounts a WHERE a.client.clientId = :clientId AND a.status = 'active'")
    List<Accounts> findActiveAccountsByClient(@Param("clientId") Integer clientId);

    // Find accounts with balance greater than
    List<Accounts> findByCurrentBalanceGreaterThan(BigDecimal balance);

    // Find account with transactions
    @Query("SELECT a FROM Accounts a LEFT JOIN FETCH a.transactions WHERE a.accountId = :id")
    Optional<Accounts> findByIdWithTransactions(@Param("id") Integer id);

    // Get total balance by client
    @Query("SELECT SUM(a.currentBalance) FROM Accounts a WHERE a.client.clientId = :clientId")
    BigDecimal getTotalBalanceByClient(@Param("clientId") Integer clientId);

    // Count accounts by type
    @Query("SELECT COUNT(a) FROM Accounts a WHERE a.accountType = :type")
    Long countByAccountType(@Param("type") String type);
}