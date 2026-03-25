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

import com.crm.dto.CustomerDto;
import com.crm.entity.AssignServicesRequest;
import com.crm.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    // ✅ CREATE CUSTOMER
    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto dto) {
        CustomerDto savedCustomer = service.createCustomer(dto);
        return ResponseEntity.ok(savedCustomer);
    }

    // ✅ UPDATE CUSTOMER
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto dto) {

        CustomerDto updatedCustomer = service.updateCustomer(id, dto);
        return ResponseEntity.ok(updatedCustomer);
    }

    // 🔥 SOFT DELETE CUSTOMER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully (Soft Delete)");
    }

    // ♻️ RESTORE CUSTOMER
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        service.restoreCustomer(id);
        return ResponseEntity.ok("Customer restored successfully");
    }

    // ✅ GET CUSTOMER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Long id) {
        CustomerDto customer = service.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // 🔥 GET ALL (Pagination + Sorting)
    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<CustomerDto> customers =
                service.getAllCustomers(page, size, sortBy, sortDir);

        return ResponseEntity.ok(customers);
    }

    // 🔍 SEARCH CUSTOMER (name / contact)
    @GetMapping("/search")
    public ResponseEntity<Page<CustomerDto>> searchCustomers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<CustomerDto> result =
                service.searchCustomers(keyword, page, size, sortBy, sortDir);

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/assign-services")
    public ResponseEntity<CustomerDto> assignServices(
            @RequestBody AssignServicesRequest request) {
        CustomerDto updated = service.assignServicesToCustomer(request.getCustomerId(), request.getServiceIds());
        return ResponseEntity.ok(updated);
    }
}