package com.crm.service.impl;

import org.springframework.stereotype.Service;

import com.crm.dto.DashboardStatsDto;
import com.crm.repo.CustomerRepository;
import com.crm.repo.InvoiceRepository;
import com.crm.repo.PaymentRepository;
import com.crm.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(CustomerRepository customerRepository,
                                InvoiceRepository invoiceRepository,
                                PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public DashboardStatsDto getDashboardStats() {
        long totalCustomers = customerRepository.count();
        long totalBookings = invoiceRepository.count(); // use invoices as bookings
        double totalRevenue = paymentRepository.sumTotalAmount();

        return new DashboardStatsDto(totalCustomers, totalBookings, totalRevenue);
    }
}