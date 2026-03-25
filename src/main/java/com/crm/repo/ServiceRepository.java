package com.crm.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Page<ServiceEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

}
