package com.wearhouse.bankproject.operational.controllers;

import com.wearhouse.bankproject.operational.dto.DashboardStatsDTO;
import com.wearhouse.bankproject.operational.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DashboardStatsDTO> adminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<DashboardStatsDTO> managerDashboard() {
        return ResponseEntity.ok(dashboardService.getManagerDashboard());
    }

    @GetMapping("/teller")
    @PreAuthorize("hasRole('teller')")
    public ResponseEntity<DashboardStatsDTO> tellerDashboard() {
        return ResponseEntity.ok(dashboardService.getTellerDashboard());
    }

    // përdoret vetëm për test login
    @GetMapping
    public ResponseEntity<DashboardStatsDTO> baseDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
}
