package com.crm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.dto.VisitDto;
import com.crm.entity.Customer;
import com.crm.entity.Visit;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.CustomerRepository;
import com.crm.repo.VisitRepository;
import com.crm.service.VisitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    // Entity → DTO
    private VisitDto mapToDto(Visit visit) {
        VisitDto dto = mapper.map(visit, VisitDto.class);
        dto.setCustomerId(visit.getCustomer().getId());
        dto.setCustomerName(visit.getCustomer().getName());
        dto.setContact(visit.getCustomer().getContact());
        return dto;
    }

    // DTO → Entity
    private Visit mapToEntity(VisitDto dto) {
        return mapper.map(dto, Visit.class);
    }

    @Override
    public VisitDto createVisit(Long customerId, VisitDto dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Visit visit = mapToEntity(dto);
        visit.setCustomer(customer);
        return mapToDto(visitRepository.save(visit));
    }

    @Override
    public VisitDto updateVisit(Long id, VisitDto dto) {
        Visit visit = visitRepository.findById(id)
                .filter(Visit::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
        visit.setVisitDate(dto.getVisitDate());
        visit.setServiceTaken(dto.getServiceTaken());
        return mapToDto(visitRepository.save(visit));
    }

    @Override
    public void deleteVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .filter(Visit::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
        visit.setActive(false);
        visit.setDeletedAt(java.time.LocalDateTime.now());
        visitRepository.save(visit);
    }

    @Override
    public void restoreVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
        visit.setActive(true);
        visit.setDeletedAt(null);
        visitRepository.save(visit);
    }

    @Override
    public VisitDto getVisitById(Long id) {
        Visit visit = visitRepository.findById(id)
                .filter(Visit::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
        return mapToDto(visit);
    }

    @Override
    public Page<VisitDto> getAllVisits(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return visitRepository.findByIsActiveTrue(pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<VisitDto> searchVisits(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return visitRepository
                .findByIsActiveTrueAndCustomer_NameContainingIgnoreCaseOrIsActiveTrueAndCustomer_ContactContaining(
                        keyword, keyword, pageable)
                .map(this::mapToDto);
    }
}