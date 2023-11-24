package com.fanstatic.repository;

import com.fanstatic.model.Product;
import com.fanstatic.model.WarehouseReceive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseReceiveRepository extends JpaRepository<WarehouseReceive, Integer> {
    public Optional<List<WarehouseReceive>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<WarehouseReceive>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<WarehouseReceive> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseReceive> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseReceive> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseReceive> findByDescriptionAndActiveIsFalse(String description);

    public List<WarehouseReceive> findAllByOrderByCreateAtDesc();

}
