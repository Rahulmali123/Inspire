package com.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.entity.Invoice;
import com.crm.entity.InvoiceItem;
import com.crm.enums.PaymentStatus;
import com.crm.repo.InvoiceRepository;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repo;

    // ✅ CREATE INVOICE
    public Invoice createInvoice(Invoice invoice) {

        // 🔹 Set default payment status
        if (invoice.getPaymentStatus() == null) {
            invoice.setPaymentStatus(PaymentStatus.PENDING);
        }

        // 🔹 Set relationship (VERY IMPORTANT)
        List<InvoiceItem> items = invoice.getItems();
        if (items != null) {
            for (InvoiceItem item : items) {
                item.setInvoice(invoice);
                item.setTotal(item.getPrice() * item.getQuantity());
            }
        }

        // 🔹 Save first time
        Invoice saved = repo.save(invoice);

        // 🔹 Generate Invoice Number
        String invoiceNumber = "INV-2026-" + String.format("%04d", saved.getId());
        saved.setInvoiceNumber(invoiceNumber);

        return repo.save(saved);
    }

    // ✅ GET INVOICE
    public Invoice getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}
