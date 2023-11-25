package com.fanstatic.dto.model.warehouseReceiveItem;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WarehouseReceiveItemDTO extends ResponseDataDTO {
    private int id;
//    private WarehouseReceiveDTO warehouseReceiveDTO;

    private FlavorDTO flavorDTO;

    private int quantity;

    private long price;

    private Date expiredAt;

}
