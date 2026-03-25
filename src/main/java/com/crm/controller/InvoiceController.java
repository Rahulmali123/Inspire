package com.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.CustomerDto;
import com.crm.dto.SendInvoiceRequest;
import com.crm.entity.Invoice;
import com.crm.service.CustomerService;
import com.crm.service.EmailService;
import com.crm.service.InvoicePdfService;
import com.crm.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoicePdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerService customerService;

    // ✅ CREATE INVOICE
    @PostMapping
    public Invoice create(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }

    // ✅ DOWNLOAD PDF
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Invoice invoice = invoiceService.getById(id);
        byte[] pdf = pdfService.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ✅ SEND EMAIL
    @GetMapping("/send/{id}")
    public String sendEmail(@PathVariable Long id) throws Exception {
        Invoice invoice = invoiceService.getById(id);

        // 1️⃣ Fetch customer info dynamically
        CustomerDto customer = customerService.getCustomerById(invoice.getCustomerId());
        String customerEmail = customer.getContact(); // assuming 'contact' field stores email
        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new RuntimeException("Customer email not found for this invoice");
        }

        // 2️⃣ Generate PDF
        byte[] pdf = pdfService.generateInvoicePdf(invoice);

        // 3️⃣ Compose email
        String subject = "Invoice #" + invoice.getInvoiceNumber() + " - Inspire CRM";
        String body = "Dear " + invoice.getCustomerName() + ",<br><br>"
                    + "Please find your invoice attached.<br>"
                    + "Invoice Amount: ₹" + invoice.getFinalAmount() + "<br><br>"
                    + "Thank you for your business!";

        // 4️⃣ Send email
        emailService.sendInvoice(customerEmail, subject, body, pdf);

        return "Invoice sent successfully to " + customerEmail + "!";
    }
    
    // ✅ Send invoice to any email (friend, yourself, etc.)
    @PostMapping("/send-custom")
    public String sendInvoiceCustom(@RequestBody SendInvoiceRequest request) throws Exception {
        Invoice invoice = invoiceService.getById(request.getInvoiceId());
        byte[] pdf = pdfService.generateInvoicePdf(invoice);

        String subject = "Invoice #" + invoice.getInvoiceNumber() + " - Inspire CRM";
        String body = "Hello,<br><br>Here is the invoice you requested.<br>"
                    + "Invoice Amount: ₹" + invoice.getFinalAmount() + "<br><br>"
                    + "Thank you!";

        emailService.sendInvoice(request.getEmail(), subject, body, pdf);

        return "Invoice sent successfully to " + request.getEmail();
    }
}