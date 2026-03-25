package com.crm.dto;

import java.time.LocalDateTime;

import com.crm.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;
    private Long customerId;
    private LocalDateTime appointmentTime;
    private Integer durationMinutes;
    private BookingStatus status;
    // Add staffId to track which staff is assigned
    private Long staffId;
    
    // Optional: convenience constructor without staffId
    public AppointmentDto(Long id, Long customerId, LocalDateTime appointmentTime, Integer durationMinutes, BookingStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
    }
    
}