package com.crm.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.dto.ReportDto;
import com.crm.repo.InvoiceRepository;
import com.crm.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    private final InvoiceRepository invoiceRepository;

    public ReportServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<ReportDto> getDailyRevenue(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.getDailyRevenueRaw(start, end).stream()
                .map(obj -> new ReportDto(obj[0].toString(), (Double) obj[1]))
                .toList();
    }

    @Override
    public List<ReportDto> getMonthlyRevenue(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.getMonthlyRevenueRaw(start, end).stream()
                .map(obj -> new ReportDto(obj[0].toString(), (Double) obj[1]))
                .toList();
    }
    
    @Override
    public List<ReportDto> getCustomerActivity(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.getCustomerActivityRaw(start, end).stream()
                .map(obj -> {
                    String label = obj[0] != null ? obj[0].toString() : "Unknown Customer";
                    Double value = ((Number) obj[1]).doubleValue();
                    return new ReportDto(label, value);
                })
                .toList();
    }


//    @Override
//    public List<ReportDto> getCustomerActivity(LocalDateTime start, LocalDateTime end) {
//        return invoiceRepository.getCustomerActivityRaw(start, end).stream()
//                .map(obj -> new ReportDto(obj[0].toString(), ((Number) obj[1]).doubleValue()))
//                .toList();
//    }

    @Override
    public List<ReportDto> getPaymentStatusReport(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.getPaymentStatusReportRaw(start, end).stream()
                .map(obj -> new ReportDto(obj[0].toString(), ((Number) obj[1]).doubleValue()))
                .toList();
    }
}