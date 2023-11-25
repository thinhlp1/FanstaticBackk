package com.fanstatic.dto.model.WarehouseDeliverReason;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverReasonRequestDTO {
    private int id;

    @NotBlank(message = "{NotBlank.warehouseDeliverReason.reason}")
    private String reason;

    @NotBlank(message = "{NotBlank.warehouseDeliverReason.code}")
    private String code;

    private String description;

//    List<WarehouseReceiveItemRequestDTO> warehouseReceiveItemRequestDTOList;
}
