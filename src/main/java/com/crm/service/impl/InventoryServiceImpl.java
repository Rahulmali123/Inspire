package com.crm.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.entity.InventoryItem;
import com.crm.repo.InventoryRepository;
import com.crm.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public InventoryItem addItem(InventoryItem item) {
        return repository.save(item);
    }

    @Override
    public InventoryItem updateItem(InventoryItem item) {
        return repository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return repository.findAll();
    }

    @Override
    public InventoryItem getItemById(Long id) {
        return repository.findById(id).orElse(null);
    }
}