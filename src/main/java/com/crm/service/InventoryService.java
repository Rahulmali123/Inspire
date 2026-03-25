package com.crm.service;

import java.util.List;

import com.crm.entity.InventoryItem;

public interface InventoryService 
{
    InventoryItem addItem(InventoryItem item);
    InventoryItem updateItem(InventoryItem item);
    void deleteItem(Long id);
    List<InventoryItem> getAllItems();
    InventoryItem getItemById(Long id);
}