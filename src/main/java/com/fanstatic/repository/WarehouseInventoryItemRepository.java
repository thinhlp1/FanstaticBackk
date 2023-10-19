package com.fanstatic.repository;

import com.fanstatic.model.WarehouseInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WarehouseInventoryItemRepository extends JpaRepository<WarehouseInventoryItem, Integer> {
}
