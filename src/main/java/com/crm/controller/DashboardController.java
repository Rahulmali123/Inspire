package com.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.DashboardStatsDto;
import com.crm.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /dashboard/stats
     * Returns total customers, total bookings (invoices), and total revenue.
     */
    @GetMapping("/stats")
    public DashboardStatsDto getStats() {
        return dashboardService.getDashboardStats();
    }
}