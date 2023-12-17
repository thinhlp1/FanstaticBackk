package com.fanstatic.dto.model.warehouseInventory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseInventoryRequestDeleteDTO {

    @NotBlank(message = "{NotBlank.warehouseInventory.cancelReason}")
    private String cancelReason;
}
