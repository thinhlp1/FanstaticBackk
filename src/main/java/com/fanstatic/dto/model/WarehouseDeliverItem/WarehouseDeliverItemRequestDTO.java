package com.fanstatic.dto.model.WarehouseDeliverItem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverItemRequestDTO {
    private int id;

    @NotNull(message = "{NotNull.warehouseDeliverItem.warehouseDeliverItemId}")
    private int warehouseDeliverItemId;

    @NotNull(message = "{NotNull.warehouseDeliverItem.flavorId}")
    private int flavorId;

    @NotNull(message = "{NotNull.warehouseDeliverItem.quantityDeliver}")
    private int quantityDeliver;
}
