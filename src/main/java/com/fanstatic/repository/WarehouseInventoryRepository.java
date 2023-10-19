package com.fanstatic.repository;

import com.fanstatic.model.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, String> {
}
