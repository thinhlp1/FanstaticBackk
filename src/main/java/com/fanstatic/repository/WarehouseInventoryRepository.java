package com.fanstatic.repository;

import com.fanstatic.model.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Integer> {
    public Optional<List<WarehouseInventory>> findAllByActiveIsTrue();

    public Optional<List<WarehouseInventory>> findAllByActiveIsFalse();

    public Optional<WarehouseInventory> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseInventory> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseInventory> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseInventory> findByDescriptionAndActiveIsFalse(String description);
}
