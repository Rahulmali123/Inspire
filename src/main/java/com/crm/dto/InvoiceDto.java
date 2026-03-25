package com.crm.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {

    private Long id;
    private Long customerId;
    private Double amount;
    private Double discount;
    private Double gst;
    private Double finalAmount;
    private String paymentStatus;
    
    private List<InvoiceItemDto> items;
}