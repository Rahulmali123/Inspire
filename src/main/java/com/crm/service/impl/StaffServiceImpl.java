package com.crm.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crm.dto.StaffDto;
import com.crm.entity.StaffEntity;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.StaffRepository;
import com.crm.service.StaffService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public StaffDto createStaff(StaffDto dto) {
        StaffEntity entity = new StaffEntity();
        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        entity.setActive(dto.isActive());

        staffRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public StaffDto updateStaff(Long id, StaffDto dto) {
        StaffEntity entity = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        entity.setActive(dto.isActive());

        staffRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public void deleteStaff(Long id) {
        StaffEntity entity = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        staffRepository.delete(entity);
    }

    @Override
    public StaffDto getStaffById(Long id) {
        StaffEntity entity = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        return toDto(entity);
    }

    @Override
    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private StaffDto toDto(StaffEntity entity) {
        return new StaffDto(
                entity.getId(),
                entity.getName(),
                entity.getRole(),
                entity.isActive()
        );
    }
}
