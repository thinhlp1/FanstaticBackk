package com.fanstatic.dto.model.warehouseDeliver;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.WarehouseDeliverItem.WarehouseDeliverItemDTO;
import com.fanstatic.dto.model.WarehouseDeliverReason.WarehouseDeliverReasonDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.warehouseDeliverSolution.WarehouseDeliverSolutionDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseDeliverDTO extends ResponseDataDTO {
    private int id;
    private WarehouseDeliverReasonDTO warehouseDeliverReasonDTO;

    private UserDTO employeeDTO;

    private WarehouseDeliverSolutionDTO warehouseDeliverSolutionDTO;

    private String description;

    private byte active;


    List<WarehouseDeliverItemDTO> warehouseDeliverItemDTOList;
}
