package com.crm.service;

import org.springframework.data.domain.Page;

import com.crm.dto.ServiceDto;

public interface ServiceManagementService 
{

    ServiceDto createService(ServiceDto dto);
    ServiceDto updateService(Long id, ServiceDto dto);
    void deleteService(Long id); // can be soft delete
    ServiceDto getServiceById(Long id);
    Page<ServiceDto> getAllServices(int page, int size, String sortBy, String sortDir);
    Page<ServiceDto> searchServices(String keyword, int page, int size, String sortBy, String sortDir);
    
    void restoreServices(Long id);
}