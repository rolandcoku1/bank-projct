package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Find transactions by account ID
    List<Transaction> findByAccountAccountId(Integer accountId);

    // Find transactions by user ID
    List<Transaction> findByUserId(Integer userId);

    // Find transactions by type
    List<Transaction> findByTransactionType(String transactionType);

    // Find transactions by date range
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find recent transactions for an account
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactionsByAccount(@Param("accountId") Integer accountId);

    // Find transactions with amount greater than
    List<Transaction> findByAmountGreaterThan(BigDecimal amount);

    // Get total transaction amount by account
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.accountId = :accountId")
    BigDecimal getTotalAmountByAccount(@Param("accountId") Integer accountId);

    // Get transaction count by type for a date range
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transactionType = :type " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    Long countByTypeAndDateRange(@Param("type") String type,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    // Find transactions by account and type
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId " +
            "AND t.transactionType = :type ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountAndType(@Param("accountId") Integer accountId,
                                           @Param("type") String type);

    // Get daily transaction summary
    @Query("SELECT DATE(t.transactionDate) as date, SUM(t.amount) as total " +
            "FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(t.transactionDate)")
    List<Object[]> getDailyTransactionSummary(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}