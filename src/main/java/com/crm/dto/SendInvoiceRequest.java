package com.crm.dto;

import lombok.Data;

@Data
public class SendInvoiceRequest {
    private Long invoiceId;
    private String email; // jya loka kade invoice send karaycha aahe
}