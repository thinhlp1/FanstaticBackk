package com.fanstatic.repository;

import com.fanstatic.model.Product;
import com.fanstatic.model.WarehouseDeliverReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseDeliverReasonRepository extends JpaRepository<WarehouseDeliverReason, Integer> {
    public Optional<List<WarehouseDeliverReason>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<WarehouseDeliverReason>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<WarehouseDeliverReason> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseDeliverReason> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseDeliverReason> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseDeliverReason> findByDescriptionAndActiveIsFalse(String description);

    public List<WarehouseDeliverReason> findAllByOrderByCreateAtDesc();

}
