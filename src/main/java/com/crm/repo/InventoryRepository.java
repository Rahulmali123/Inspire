package com.crm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

}
