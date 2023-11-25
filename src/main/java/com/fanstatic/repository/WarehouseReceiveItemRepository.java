package com.fanstatic.repository;

import com.fanstatic.model.WarehouseReceiveItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WarehouseReceiveItemRepository extends JpaRepository<WarehouseReceiveItem, Integer> {
//    public Optional<List<WarehouseReceiveItem>> findAllBy();
//
//    public Optional<List<WarehouseReceiveItem>> findAllB();

    public Optional<WarehouseReceiveItem> findById(int id);


}
