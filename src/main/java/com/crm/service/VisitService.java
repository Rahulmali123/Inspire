package com.crm.service;

import org.springframework.data.domain.Page;

import com.crm.dto.VisitDto;

public interface VisitService {

    VisitDto createVisit(Long customerId, VisitDto dto);

    VisitDto updateVisit(Long id, VisitDto dto);

    void deleteVisit(Long id); // Soft delete

    void restoreVisit(Long id); // Restore soft-deleted visit

    VisitDto getVisitById(Long id);

    Page<VisitDto> getAllVisits(int page, int size, String sortBy, String sortDir);

    Page<VisitDto> searchVisits(String keyword, int page, int size, String sortBy, String sortDir);
}