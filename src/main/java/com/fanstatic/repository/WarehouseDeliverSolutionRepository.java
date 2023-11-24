package com.fanstatic.repository;

import com.fanstatic.model.WarehouseDeliverSolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseDeliverSolutionRepository extends JpaRepository<WarehouseDeliverSolution, Integer> {
    public Optional<List<WarehouseDeliverSolution>> findAllByActiveIsTrue();

    public Optional<List<WarehouseDeliverSolution>> findAllByActiveIsFalse();

    public Optional<WarehouseDeliverSolution> findByIdAndActiveIsTrue(int id);

    public Optional<WarehouseDeliverSolution> findByIdAndActiveIsFalse(int id);

    public Optional<WarehouseDeliverSolution> findByDescriptionAndActiveIsTrue(String description);

    public Optional<WarehouseDeliverSolution> findByDescriptionAndActiveIsFalse(String description);
}
