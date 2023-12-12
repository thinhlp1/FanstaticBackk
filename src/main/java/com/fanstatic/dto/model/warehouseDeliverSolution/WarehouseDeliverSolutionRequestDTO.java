package com.fanstatic.dto.model.warehouseDeliverSolution;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverSolutionRequestDTO {

    @NotBlank(message = "{NotBlank.warehouseDeliverSolution.solution}")
    private String solution;

    @NotBlank(message = "{NotBlank.warehouseDeliverSolution.code}")
    private String code;

    private String description;


//    List<WarehouseReceiveItemRequestDTO> warehouseReceiveItemRequestDTOList;
}
