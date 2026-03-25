package com.crm.service;

import com.crm.dto.InvoiceDto;
import com.crm.dto.PaymentDto;

public interface BillingService {

    InvoiceDto generateInvoice(InvoiceDto dto);

    PaymentDto makePayment(PaymentDto dto);

    InvoiceDto getInvoice(Long id);
    
    byte[] downloadInvoicePdf(Long invoiceId);
}