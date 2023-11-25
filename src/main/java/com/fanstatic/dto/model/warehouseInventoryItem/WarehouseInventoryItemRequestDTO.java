package com.fanstatic.dto.model.warehouseInventoryItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseInventoryItemRequestDTO {
    private int id;
    @NotNull(message = "{NotNull.warehouseInventory.flavorId}")
    private int flavorId;

    @NotNull(message = "{NotNull.warehouseInventory.warehouseInventoryId}")
    private int warehouseInventoryId;
    @NotNull(message = "{NotNull.warehouseInventory.quantityInPaper}")
    private int quantityInPaper;

    @NotNull(message = "{NotNull.warehouseInventory.quantityInInventory}")
    private int quantityInInventory;

    private int quantityDifferent;

    private String reason;

    @NotBlank(message = "{NotBlank.warehouseInventory.solution}")
    private String solution;


//    List<WarehouseInventoryItemRequestDTO> warehouseInventoryItemRequestDTOList;
}
