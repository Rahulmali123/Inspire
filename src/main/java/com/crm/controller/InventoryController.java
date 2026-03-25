package com.crm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.entity.InventoryItem;
import com.crm.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public InventoryItem addItem(@RequestBody InventoryItem item) {
        return service.addItem(item);
    }

    @PutMapping("/update")
    public InventoryItem updateItem(@RequestBody InventoryItem item) {
        return service.updateItem(item);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
    }

    @GetMapping("/all")
    public List<InventoryItem> getAllItems() {
        return service.getAllItems();
    }

    @GetMapping("/{id}")
    public InventoryItem getItem(@PathVariable Long id) {
        return service.getItemById(id);
    }
}