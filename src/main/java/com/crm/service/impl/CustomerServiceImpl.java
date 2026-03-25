package com.crm.service.impl;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.dto.CustomerDto;
import com.crm.dto.ServiceDto;
import com.crm.entity.Customer;
import com.crm.entity.ServiceEntity;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.CustomerRepository;
import com.crm.repo.ServiceRepository;
import com.crm.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final ServiceRepository serviceRepo;
    private final ModelMapper mapper;

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        Customer customer = mapper.map(dto, Customer.class);
        Customer saved = customerRepo.save(customer);
        return mapToDto(saved);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer customer = customerRepo.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setName(dto.getName());
        customer.setContact(dto.getContact());
        customer.setMembership(dto.getMembership());
        customer.setPreferences(dto.getPreferences());
        customer.setUpdatedAt(LocalDateTime.now());

        return mapToDto(customerRepo.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepo.save(customer);
    }

    @Override
    public void restoreCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setActive(true);
        customer.setDeletedAt(null);
        customerRepo.save(customer);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepo.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return mapToDto(customer);
    }

    @Override
    public Page<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return customerRepo.findByIsActiveTrue(pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<CustomerDto> searchCustomers(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return customerRepo.searchActiveCustomers(keyword, pageable)
                .map(this::mapToDto);
    }

    @Override
    public CustomerDto assignServicesToCustomer(Long customerId, Set<Long> serviceIds) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Set<ServiceEntity> services = serviceIds.stream()
                .map(id -> serviceRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id)))
                .collect(Collectors.toSet());

        customer.setAssignedServices(services);
        return mapToDto(customerRepo.save(customer));
    }

   
    private CustomerDto mapToDto(Customer customer) {
        CustomerDto dto = mapper.map(customer, CustomerDto.class);
        if (customer.getAssignedServices() != null && !customer.getAssignedServices().isEmpty()) {
            Set<ServiceDto> servicesDto = customer.getAssignedServices().stream()
                .map(service -> mapper.map(service, ServiceDto.class))
                .collect(Collectors.toSet());
            dto.setAssignedServices(servicesDto);
        }
        return dto;
    }
}