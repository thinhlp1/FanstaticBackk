package com.fanstatic.dto.model.WarehouseDeliverReason;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverReasonDTO extends ResponseDataDTO {
    private int id;
    private String reason;

    private String code;

    private String description;

    private boolean active;


//    List<WarehouseReceiveItemDTO> warehouseReceiveItemDTOList;
}
