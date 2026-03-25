package com.crm.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.InvoiceDto;
import com.crm.dto.PaymentDto;
import com.crm.service.BillingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoice")
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto dto) {
        return ResponseEntity.ok(billingService.generateInvoice(dto));
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentDto> makePayment(@RequestBody PaymentDto dto) {
        return ResponseEntity.ok(billingService.makePayment(dto));
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getInvoice(id));
    }
    
    @GetMapping("/invoice/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {

        byte[] pdf = billingService.downloadInvoicePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}