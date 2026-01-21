package com.wearhouse.bankproject.risk.repository;

import com.wearhouse.bankproject.risk.entity.PortfolioRiskMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRiskRepository extends JpaRepository<PortfolioRiskMetric, Long> {
    // Standard CRUD
}