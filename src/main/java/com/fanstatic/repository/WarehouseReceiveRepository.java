package com.fanstatic.repository;

import com.fanstatic.model.WarehouseReceive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WarehouseReceiveRepository extends JpaRepository<WarehouseReceive, Integer> {
    public Optional<List<WarehouseReceive>> findAllByActiveIsTrue();

    public Optional<List<WarehouseReceive>> findAllByActiveIsFalse();

    public Optional<WarehouseReceive> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseReceive> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseReceive> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseReceive> findByDescriptionAndActiveIsFalse(String description);

}
