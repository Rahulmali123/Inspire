package com.crm.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.crm.dto.CustomerDto;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto dto);

    CustomerDto updateCustomer(Long id, CustomerDto dto);

    void deleteCustomer(Long id);

    CustomerDto getCustomerById(Long id);

    Page<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir);

    Page<CustomerDto> searchCustomers(String keyword, int page, int size, String sortBy, String sortDir);
    
    void restoreCustomer(Long id);
    
    CustomerDto assignServicesToCustomer(Long customerId, Set<Long> serviceIds);
}