package com.crm.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.AppointmentDto;
import com.crm.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    // --- Create appointment ---
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(service.createAppointment(dto));
    }

    // --- Update appointment status ---
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateAppointmentStatus(id, status));
    }

    // --- Get appointments by customer ---
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AppointmentDto>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getAppointmentsByCustomer(customerId));
    }

    // --- Get appointments by time slot ---
    @GetMapping("/timeslot")
    public ResponseEntity<List<AppointmentDto>> getByTimeSlot(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(service.getAppointmentsByTimeSlot(start, end));
    }

    // --- Get available slots for a given date and duration ---
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam Integer durationMinutes) {
        List<LocalDateTime> slots = service.getAvailableSlots(date, durationMinutes);
        return ResponseEntity.ok(slots);
    }
}