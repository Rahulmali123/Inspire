package com.crm.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class VisitDto {

    private Long id;

    @NotNull(message = "Visit date is required")
    @PastOrPresent(message = "Visit date cannot be in the future")
    private LocalDate visitDate;

    @NotBlank(message = "Service taken is required")
    private String serviceTaken;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String customerName;
    private String contact;
}