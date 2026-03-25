package com.crm.service;

import java.time.LocalDateTime;
import java.util.List;

import com.crm.dto.ReportDto;

public interface ReportService {

    List<ReportDto> getDailyRevenue(LocalDateTime start, LocalDateTime end);

    List<ReportDto> getMonthlyRevenue(LocalDateTime start, LocalDateTime end);

    List<ReportDto> getCustomerActivity(LocalDateTime start, LocalDateTime end);

    List<ReportDto> getPaymentStatusReport(LocalDateTime start, LocalDateTime end);
}