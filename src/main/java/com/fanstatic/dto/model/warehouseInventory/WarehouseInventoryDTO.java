package com.fanstatic.dto.model.warehouseInventory;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.flavorcategory.FlavorCategoryDTO;
import com.fanstatic.dto.model.warehouseInventoryItem.WarehouseInventoryItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseInventoryDTO extends ResponseDataDTO {
    private int id;
    private FlavorCategoryDTO flavorCategoryDTO;

    private String description;

    private Date toDate;

    private boolean active;

    List<WarehouseInventoryItemDTO> warehouseInventoryItemDTOList;
}
