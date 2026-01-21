package com.wearhouse.bankproject.historical.repository;

import com.wearhouse.bankproject.historical.entity.FactLoanPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactLoanRepository extends JpaRepository<FactLoanPerformance, Long> {
    // Standard CRUD
}