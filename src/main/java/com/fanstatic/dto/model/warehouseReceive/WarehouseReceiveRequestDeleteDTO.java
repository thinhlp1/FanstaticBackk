package com.fanstatic.dto.model.warehouseReceive;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseReceiveRequestDeleteDTO {

    @NotBlank(message = "{NotBlank.warehouseReceive.cancelReason}")
    private String cancelReason;
}
