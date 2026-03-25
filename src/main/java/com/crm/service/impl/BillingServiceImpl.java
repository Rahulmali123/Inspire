package com.crm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.dto.InvoiceDto;
import com.crm.dto.InvoiceItemDto;
import com.crm.dto.PaymentDto;
import com.crm.entity.Invoice;
import com.crm.entity.InvoiceItem;
import com.crm.entity.Payment;
import com.crm.enums.PaymentMethod;
import com.crm.enums.PaymentStatus;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.InvoiceRepository;
import com.crm.repo.PaymentRepository;
import com.crm.service.BillingService;
import com.crm.service.InvoicePdfService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;
    private final InvoicePdfService pdfService;

    // =============================
    // 🔹 GENERATE INVOICE
    // =============================
    @Override
    public InvoiceDto generateInvoice(InvoiceDto dto) {

        double discount = dto.getDiscount() != null ? dto.getDiscount() : 0;
        double gst = dto.getGst() != null ? dto.getGst() : 0;

        Invoice invoice = new Invoice();
        invoice.setCustomerId(dto.getCustomerId());
        invoice.setPaymentStatus(PaymentStatus.PENDING);

        List<InvoiceItem> items = new ArrayList<>();
        double totalAmount = 0;

        for (InvoiceItemDto itemDto : dto.getItems()) {

            InvoiceItem item = new InvoiceItem();

            item.setServiceName(itemDto.getServiceName());
            item.setPrice(itemDto.getPrice());
            item.setQuantity(itemDto.getQuantity());

            double itemTotal = itemDto.getPrice() * itemDto.getQuantity();
            item.setTotal(itemTotal);

            item.setInvoice(invoice);
            items.add(item);

            totalAmount += itemTotal;
        }

        // calculations
        double discountAmount = totalAmount * (discount / 100);
        double afterDiscount = totalAmount - discountAmount;

        double gstAmount = afterDiscount * (gst / 100);
        double finalAmount = afterDiscount + gstAmount;

        invoice.setAmount(totalAmount);
        invoice.setDiscount(discount);
        invoice.setGst(gst);
        invoice.setFinalAmount(finalAmount);
        invoice.setItems(items);

        Invoice saved = invoiceRepo.save(invoice);

        return mapToDto(saved);
    }

    // =============================
    // 🔹 MAKE PAYMENT
    // =============================
    @Override
    public PaymentDto makePayment(PaymentDto dto) {

        Invoice invoice = invoiceRepo.findById(dto.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        Double amountToPay = invoice.getFinalAmount();

        Payment payment = new Payment();
        payment.setInvoiceId(dto.getInvoiceId());
        payment.setAmount(amountToPay);
        payment.setMethod(PaymentMethod.valueOf(dto.getMethod()));
        payment.setPaymentDate(LocalDateTime.now());

        Payment saved = paymentRepo.save(payment);

        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoiceRepo.save(invoice);

        return new PaymentDto(
                saved.getId(),
                saved.getInvoiceId(),
                saved.getAmount(),
                saved.getMethod().name()
        );
    }

    // =============================
    // 🔹 GET INVOICE
    // =============================
    @Override
    public InvoiceDto getInvoice(Long id) {

        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return mapToDto(invoice);
    }
    
    
    public byte[] downloadInvoicePdf(Long invoiceId) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return pdfService.generateInvoicePdf(invoice);
    }

    // =============================
    // 🔹 MAPPER
    // =============================
    private InvoiceDto mapToDto(Invoice i) {

        List<InvoiceItemDto> itemDtos = i.getItems().stream()
                .map(item -> new InvoiceItemDto(
                        item.getServiceName(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .toList();

        return new InvoiceDto(
                i.getId(),
                i.getCustomerId(),
                i.getAmount(),
                i.getDiscount(),
                i.getGst(),
                i.getFinalAmount(),
                i.getPaymentStatus().name(),
                itemDtos
        );
    }
}