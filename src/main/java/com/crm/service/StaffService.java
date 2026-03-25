package com.crm.service;

import java.util.List;

import com.crm.dto.StaffDto;

public interface StaffService {

    StaffDto createStaff(StaffDto dto);

    StaffDto updateStaff(Long id, StaffDto dto);

    void deleteStaff(Long id);

    StaffDto getStaffById(Long id);

    List<StaffDto> getAllStaff();
}