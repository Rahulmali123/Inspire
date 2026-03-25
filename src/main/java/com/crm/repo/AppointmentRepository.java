package com.crm.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.entity.AppointmentEntity;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    // Find all appointments for a customer
    List<AppointmentEntity> findByCustomerId(Long customerId);

    // Find appointments in a given time slot
    List<AppointmentEntity> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);
    
    
    List<AppointmentEntity> findByStaffId(Long staffId);

}
