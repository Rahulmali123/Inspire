package com.crm.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.ServiceDto;
import com.crm.service.ServiceManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceManagementService service;

    @PostMapping
    public ResponseEntity<ServiceDto> create(@Valid @RequestBody ServiceDto dto) {
        return ResponseEntity.ok(service.createService(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> update(@PathVariable Long id, @Valid @RequestBody ServiceDto dto) {
        return ResponseEntity.ok(service.updateService(id, dto));
    }
    
    // ♻️ RESTORE CUSTOMER
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id)
    {
    	service.restoreServices(id);
    	return ResponseEntity.ok("Service restored successfully");
    }

    // 🔥 SOFT DELETE CUSTOMER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteService(id);
        return ResponseEntity.ok("Service deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getServiceById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ServiceDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(service.getAllServices(page, size, sortBy, sortDir));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ServiceDto>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(service.searchServices(keyword, page, size, sortBy, sortDir));
    }
}