package com.wearhouse.bankproject.operational.services;
import com.wearhouse.bankproject.operational.dto.DashboardStatsDTO;

public interface DashboardService {
    DashboardStatsDTO getDashboardStats();
    DashboardStatsDTO getAdminDashboard();
    DashboardStatsDTO getManagerDashboard();
    DashboardStatsDTO getTellerDashboard();
}
