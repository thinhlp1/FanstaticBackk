package com.fanstatic.repository;

import com.fanstatic.model.WarehouseDeliver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WarehouseDeliverRepository extends JpaRepository<WarehouseDeliver, Integer> {
    public Optional<List<WarehouseDeliver>> findAllByActiveIsTrue();

    public Optional<List<WarehouseDeliver>> findAllByActiveIsFalse();

    public Optional<WarehouseDeliver> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseDeliver> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseDeliver> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseDeliver> findByDescriptionAndActiveIsFalse(String description);
}
