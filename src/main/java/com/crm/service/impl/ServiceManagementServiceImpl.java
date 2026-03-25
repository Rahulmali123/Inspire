package com.crm.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.dto.ServiceDto;
import com.crm.entity.ServiceEntity;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.ServiceRepository;
import com.crm.service.ServiceManagementService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceManagementServiceImpl implements ServiceManagementService {

	private final ServiceRepository repository;
	private final ModelMapper mapper;

	// ✅ CREATE SERVICE
	@Override
	public ServiceDto createService(ServiceDto dto) {
		ServiceEntity service = mapper.map(dto, ServiceEntity.class);

		// optional: explicitly set timestamps
		LocalDateTime now = LocalDateTime.now();
		service.setCreatedAt(now);
		service.setUpdatedAt(now);

		ServiceEntity saved = repository.save(service);
		return mapper.map(saved, ServiceDto.class);
	}

	// ✅ UPDATE SERVICE
	@Override
	public ServiceDto updateService(Long id, ServiceDto dto) {
		ServiceEntity service = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));

		service.setName(dto.getName());
		service.setCategory(dto.getCategory());
		service.setPrice(dto.getPrice());
		service.setDuration(dto.getDuration());

		// explicitly update timestamp
		service.setUpdatedAt(LocalDateTime.now());

		return mapper.map(repository.save(service), ServiceDto.class);
	}

	// 🔥 DELETE SERVICE (soft delete optional)
	@Transactional
	@Override
	public void deleteService(Long id) {
	    ServiceEntity entity = repository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Service not found"));
	    entity.setActive(false);       // Lombok setter
	    entity.setDeletedAt(LocalDateTime.now());
	    repository.save(entity);
	}

	@Transactional
	@Override
	public void restoreServices(Long id) {
	    ServiceEntity entity = repository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Service not found"));
	    entity.setActive(true);
	    entity.setDeletedAt(null);
	    repository.save(entity);
	}

	// ✅ GET SERVICE BY ID
	@Override
	public ServiceDto getServiceById(Long id) {
		ServiceEntity service = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));

		return mapper.map(service, ServiceDto.class);
	}

	// 🔥 GET ALL SERVICES (Pagination + Sorting)
	@Override
	public Page<ServiceDto> getAllServices(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return repository.findAll(pageable).map(service -> mapper.map(service, ServiceDto.class));
	}

	// 🔍 SEARCH SERVICES BY NAME
	@Override
	public Page<ServiceDto> searchServices(String keyword, int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return repository.findByNameContainingIgnoreCase(keyword, pageable)
				.map(service -> mapper.map(service, ServiceDto.class));
	}

}
