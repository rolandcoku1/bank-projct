package com.wearhouse.bankproject.operational.repository;
import com.wearhouse.bankproject.operational.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loans, Integer> {

    // Find loans by client ID
    List<Loans> findByClientClientId(Integer clientId);

    // Find loans by status
    List<Loans> findByStatus(String status);

    // Find active loans for a client
    @Query("SELECT l FROM Loans l WHERE l.client.clientId = :clientId AND l.status = 'active'")
    List<Loans> findActiveLoansForClient(@Param("clientId") Integer clientId);

    // Find loans with amount greater than
    List<Loans> findByLoanAmountGreaterThan(BigDecimal amount);

    // Find loans by date range
    List<Loans> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    // Get total loan amount for a client
    @Query("SELECT SUM(l.loanAmount) FROM Loans l WHERE l.client.clientId = :clientId")
    BigDecimal getTotalLoanAmountByClient(@Param("clientId") Integer clientId);

    // Find loans with high interest rate
    @Query("SELECT l FROM Loans l WHERE l.interestRate > :rate ORDER BY l.interestRate DESC")
    List<Loans> findLoansWithHighInterestRate(@Param("rate") BigDecimal rate);

    // Count loans by status
    Long countByStatus(String status);
}
