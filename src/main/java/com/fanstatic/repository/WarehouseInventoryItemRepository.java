package com.fanstatic.repository;

import com.fanstatic.model.WarehouseInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WarehouseInventoryItemRepository extends JpaRepository<WarehouseInventoryItem, Integer> {
    public Optional<List<WarehouseInventoryItem>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<WarehouseInventoryItem>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<WarehouseInventoryItem> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseInventoryItem> findByIdAndActiveIsFalse(int id);

}
