package com.crm.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMembershipDto {

    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Plan ID is required")
    private Long planId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active; // ✅ FIX
}