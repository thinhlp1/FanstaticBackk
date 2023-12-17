package com.fanstatic.dto.model.warehouseInventory;

import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemRequestDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseInventoryRequestDTO {

    @NotNull(message = "{NotNull.warehouseInventory.flavorCategoryId}")
    private int flavorCategoryId;

    @NotBlank(message = "{NotBlank.warehouseInventory.description}")
    private String description;

    @Future(message = "{Future.warehouseInventory.toDate}")
    private Date toDate;

    List<WarehouseInventoryItemRequestDTO> warehouseInventoryItemRequestDTOList;
}
