package com.fanstatic.dto.model.WarehouseDeliverItem;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseDeliverItemDTO extends ResponseDataDTO {
    private int id;

    private FlavorDTO flavorDTO;

    private int quantityDeliver;

}
