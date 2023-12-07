package com.fanstatic.repository;

import com.fanstatic.model.WarehouseReceiveItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WarehouseReceiveItemRepository extends JpaRepository<WarehouseReceiveItem, Integer> {
    public Optional<List<WarehouseReceiveItem>> findAllByFlavorId(int flavorId);
//
//    public Optional<List<WarehouseReceiveItem>> findAllB();

    public Optional<WarehouseReceiveItem> findById(int id);

    // Đếm quantity theo một danh sách flavor id
    // Đếm quantity theo flavor id
    int countByFlavorId(int flavorId);


}
