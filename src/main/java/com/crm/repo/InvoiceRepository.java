package com.crm.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crm.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Daily revenue raw
    @Query("SELECT FUNCTION('DATE', i.createdAt), SUM(i.finalAmount) " +
           "FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end " +
           "GROUP BY FUNCTION('DATE', i.createdAt)")
    List<Object[]> getDailyRevenueRaw(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    // Monthly revenue raw
    @Query("SELECT FUNCTION('MONTH', i.createdAt), SUM(i.finalAmount) " +
           "FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end " +
           "GROUP BY FUNCTION('MONTH', i.createdAt)")
    List<Object[]> getMonthlyRevenueRaw(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    // Customer activity raw
    @Query("SELECT i.customerName, COUNT(i) " +
           "FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end " +
           "GROUP BY i.customerName")
    List<Object[]> getCustomerActivityRaw(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    // Payment status raw
    @Query("SELECT i.paymentStatus, COUNT(i) " +
           "FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end " +
           "GROUP BY i.paymentStatus")
    List<Object[]> getPaymentStatusReportRaw(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}