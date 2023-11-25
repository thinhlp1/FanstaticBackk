package com.fanstatic.dto.model.warehouseDeliver;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverRequestDeleteDTO {

    @NotBlank(message = "{NotBlank.warehouseDeliver.cancelReason}")
    private String cancelReason;
}
