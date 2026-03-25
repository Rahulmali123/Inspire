package com.crm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.CustomerMembership;

public interface CustomerMembershipRepository extends JpaRepository<CustomerMembership, Long> {

    List<CustomerMembership> findByCustomerId(Long customerId);
}
