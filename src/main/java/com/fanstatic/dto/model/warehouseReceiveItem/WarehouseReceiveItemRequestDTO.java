package com.fanstatic.dto.model.warehouseReceiveItem;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WarehouseReceiveItemRequestDTO {
    private int id;

    private int warehouseReceiveItemId;

    @NotNull(message = "{NotNull.warehouseReceiveItem.flavorId}")
    private int flavorId;

    @NotNull(message = "{NotNull.warehouseReceiveItem.quantity}")
    private int quantity;

    @NotNull(message = "{NotNull.warehouseReceiveItem.price}")
    private long price;

    @Future(message = "{Future.warehouseReceiveItem.expiredAt}")
    private Date expiredAt;
}
