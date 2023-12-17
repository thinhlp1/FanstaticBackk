package com.fanstatic.dto.model.warehouseDeliver;

import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseDeliverRequestDTO {
    private int id;

    @NotNull(message = "{NotNull.warehouseDeliver.reasonId}")
    private int reasonId;

    @NotNull(message = "{NotNull.warehouseDeliver.employeeId}")
    private int employeeId;

    @NotNull(message = "{NotNull.warehouseDeliver.solutionId}")
    private int solutionId;

    @NotBlank(message = "{NotBlank.warehouseDeliver.description}")
    private String description;

    List<WarehouseDeliverItemRequestDTO> warehouseDeliverItemRequestDTOList;
}
