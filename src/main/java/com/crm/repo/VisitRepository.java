package com.crm.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByCustomerId(Long customerId);

    Page<Visit> findByIsActiveTrue(Pageable pageable);

    Page<Visit> findByIsActiveTrueAndCustomer_NameContainingIgnoreCaseOrIsActiveTrueAndCustomer_ContactContaining(
            String name, String contact, Pageable pageable);
}