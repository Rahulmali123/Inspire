package com.crm.service;

import java.time.LocalDateTime;
import java.util.List;

import com.crm.dto.AppointmentDto;

public interface AppointmentService
{

    AppointmentDto createAppointment(AppointmentDto dto);

    AppointmentDto updateAppointmentStatus(Long appointmentId, String status);

    List<AppointmentDto> getAppointmentsByCustomer(Long customerId);

    List<AppointmentDto> getAppointmentsByTimeSlot(String start, String end);
    
    boolean isTimeSlotAvailable(Long customerId, LocalDateTime startTime, Integer durationMinutes);
    
    Long findAvailableStaff(LocalDateTime startTime, Integer durationMinutes);
    
    // Add this line to fix the @Override error
    List<LocalDateTime> getAvailableSlots(LocalDateTime date, Integer durationMinutes);
}