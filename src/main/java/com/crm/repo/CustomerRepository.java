package com.crm.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crm.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	// 🔥 Only ACTIVE records
	Page<Customer> findByIsActiveTrue(Pageable pageable);

	// 🔍 Search only ACTIVE
	Page<Customer> findByIsActiveTrueAndNameContainingIgnoreCaseOrIsActiveTrueAndContactContaining(String name,
			String contact, Pageable pageable);

	@Query("SELECT c FROM Customer c WHERE c.isActive = true AND "
			+ "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR c.contact LIKE CONCAT('%', :keyword, '%'))")
	Page<Customer> searchActiveCustomers(@Param("keyword") String keyword, Pageable pageable);
}


