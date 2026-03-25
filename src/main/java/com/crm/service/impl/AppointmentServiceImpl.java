package com.crm.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crm.dto.AppointmentDto;
import com.crm.entity.AppointmentEntity;
import com.crm.entity.StaffEntity;
import com.crm.enums.BookingStatus;
import com.crm.exception.NoStaffAvailableException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.AppointmentRepository;
import com.crm.repo.StaffRepository;
import com.crm.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository repository;
	private final StaffRepository staffRepository;

	// --- Create appointment with automatic staff assignment ---
	@Override
	public AppointmentDto createAppointment(AppointmentDto dto) {
		Long staffId = findAvailableStaff(dto.getAppointmentTime(), dto.getDurationMinutes());

		if (staffId == null) {
			throw new NoStaffAvailableException("No staff available at the selected time slot");
		}

		StaffEntity staff = staffRepository.findById(staffId)
				.orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		AppointmentEntity entity = new AppointmentEntity();
		entity.setCustomerId(dto.getCustomerId());
		entity.setAppointmentTime(dto.getAppointmentTime());
		entity.setDurationMinutes(dto.getDurationMinutes());
		entity.setStatus(dto.getStatus() != null ? dto.getStatus() : BookingStatus.BOOKED);
		entity.setStaff(staff);

		repository.save(entity);

		dto.setId(entity.getId());
		dto.setStaffId(staff.getId());
		dto.setStatus(entity.getStatus());
		return dto;
	}

	// --- Check if customer time slot is free ---
	@Override
	public boolean isTimeSlotAvailable(Long customerId, LocalDateTime startTime, Integer durationMinutes) {
		LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
		List<AppointmentEntity> existingAppointments = repository.findByCustomerId(customerId);

		return existingAppointments.stream().noneMatch(appt -> {
			LocalDateTime apptStart = appt.getAppointmentTime();
			LocalDateTime apptEnd = apptStart.plusMinutes(appt.getDurationMinutes());
			return startTime.isBefore(apptEnd) && endTime.isAfter(apptStart);
		});
	}

	// --- Update appointment status ---
	@Override
	public AppointmentDto updateAppointmentStatus(Long appointmentId, String status) {
		AppointmentEntity entity = repository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
		entity.setStatus(BookingStatus.valueOf(status));
		repository.save(entity);

		AppointmentDto dto = new AppointmentDto();
		dto.setId(entity.getId());
		dto.setCustomerId(entity.getCustomerId());
		dto.setAppointmentTime(entity.getAppointmentTime());
		dto.setDurationMinutes(entity.getDurationMinutes());
		dto.setStatus(entity.getStatus());
		dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getId() : null);
		return dto;
	}

	// --- Get appointments by customer ---
	@Override
	public List<AppointmentDto> getAppointmentsByCustomer(Long customerId) {
		return repository.findByCustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
	}

	// --- Get appointments by time slot ---
	@Override
	public List<AppointmentDto> getAppointmentsByTimeSlot(String start, String end) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalDateTime startTime = LocalDateTime.parse(start, formatter);
		LocalDateTime endTime = LocalDateTime.parse(end, formatter);

		return repository.findByAppointmentTimeBetween(startTime, endTime).stream().map(this::toDto)
				.collect(Collectors.toList());
	}

	// --- Convert entity to DTO ---
	private AppointmentDto toDto(AppointmentEntity entity) {
		AppointmentDto dto = new AppointmentDto();
		dto.setId(entity.getId());
		dto.setCustomerId(entity.getCustomerId());
		dto.setAppointmentTime(entity.getAppointmentTime());
		dto.setDurationMinutes(entity.getDurationMinutes());
		dto.setStatus(entity.getStatus());
		dto.setStaffId(entity.getStaff() != null ? entity.getStaff().getId() : null);
		return dto;
	}

	// --- Find available staff for a given slot ---
	@Override
	public Long findAvailableStaff(LocalDateTime startTime, Integer durationMinutes) {
		List<StaffEntity> staffList = staffRepository.findAll().stream().filter(StaffEntity::isActive)
				.collect(Collectors.toList());

		for (StaffEntity staff : staffList) {
			boolean available = repository.findByStaffId(staff.getId()).stream().noneMatch(appt -> {
				LocalDateTime apptStart = appt.getAppointmentTime();
				LocalDateTime apptEnd = apptStart.plusMinutes(appt.getDurationMinutes());
				LocalDateTime reqEnd = startTime.plusMinutes(durationMinutes);
				return startTime.isBefore(apptEnd) && reqEnd.isAfter(apptStart);
			});

			if (available)
				return staff.getId();
		}
		throw new RuntimeException("No staff available at the selected time slot");
	}

	// --- Get available slots for frontend calendar ---
	@Override
	public List<LocalDateTime> getAvailableSlots(LocalDateTime date, Integer durationMinutes) {
		// Step 1: Get only active staff
		List<StaffEntity> staffList = staffRepository.findAll().stream().filter(StaffEntity::isActive)
				.collect(Collectors.toList());

		// Step 2: If no staff available at all
		if (staffList.isEmpty()) {
			throw new NoStaffAvailableException("No active staff available in the system");
		}

		 // Step 3: Loop through time slots
		List<LocalDateTime> availableSlots = new ArrayList<>();
		LocalDateTime startOfDay = date.withHour(9).withMinute(0); // 9 AM
		LocalDateTime endOfDay = date.withHour(18).withMinute(0); // 6 PM
		LocalDateTime slotTime = startOfDay;

		while (slotTime.plusMinutes(durationMinutes).isBefore(endOfDay)) {
			final LocalDateTime currentSlot = slotTime; // final copy for lambda

			// Step 4: Check if ANY staff is free
	        boolean isAnyStaffAvailable = staffList.stream().anyMatch(staff -> {

	            List<AppointmentEntity> staffAppointments = repository.findByStaffId(staff.getId());

	            return staffAppointments.stream().noneMatch(appt -> {
	                LocalDateTime apptStart = appt.getAppointmentTime();
	                LocalDateTime apptEnd = apptStart.plusMinutes(appt.getDurationMinutes());
	                LocalDateTime reqEnd = currentSlot.plusMinutes(durationMinutes);

	                return currentSlot.isBefore(apptEnd) && reqEnd.isAfter(apptStart);
	            });
	        });

	        // Step 5: If at least one staff is free → slot is available
	        if (isAnyStaffAvailable) {
	            availableSlots.add(currentSlot);
	        }

	        // Step 6: Move to next slot (30 mins)
	        slotTime = slotTime.plusMinutes(30);
	    }

	    return availableSlots;
	}
}