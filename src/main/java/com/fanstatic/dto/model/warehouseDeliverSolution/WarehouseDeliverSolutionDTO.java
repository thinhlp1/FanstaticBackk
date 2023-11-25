package com.fanstatic.dto.model.warehouseDeliverSolution;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverSolutionDTO extends ResponseDataDTO {
    private int id;
    private String solution;


    private String code;

    private String description;

    private boolean active;


//    List<WarehouseReceiveItemDTO> warehouseReceiveItemDTOList;
}
