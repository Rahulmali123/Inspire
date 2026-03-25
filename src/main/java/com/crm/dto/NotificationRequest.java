package com.crm.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long customerId;
    private String message;
}