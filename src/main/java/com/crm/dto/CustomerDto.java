package com.crm.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.crm.enums.Membership;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    private String contact;

    @NotNull(message = "Membership is required")
    private Membership membership;

    @NotBlank(message = "Preferences cannot be empty")
    @Size(max = 200)
    private String preferences;
    
    // ✅ Use DTO here, not entity
    private Set<ServiceDto> assignedServices;
    
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private boolean isActive;
}