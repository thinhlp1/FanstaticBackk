package com.fanstatic.dto.model.warehouseInventoryItem;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.flavor.FlavorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseInventoryItemDTO extends ResponseDataDTO {
    private int id;
    private FlavorDTO flavorDTO;

    private int quantityInPaper;

    private int quantityInInventory;

    private int quantityDifferent;

    private String reason;

    private String solution;


    private boolean active;


//    List<WarehouseInventoryItemDTO> warehouseInventoryItemDTOList;
}
