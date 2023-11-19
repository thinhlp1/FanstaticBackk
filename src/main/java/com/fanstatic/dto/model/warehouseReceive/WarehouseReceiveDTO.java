package com.fanstatic.dto.model.warehouseReceive;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseReceiveDTO extends ResponseDataDTO {
    private int id;
    private SupplierDTO supplierDTO;

    private String imageFileUrl;

    private String description;


    private boolean active;

    List<WarehouseReceiveItemDTO> warehouseReceiveItemDTOList;
}
