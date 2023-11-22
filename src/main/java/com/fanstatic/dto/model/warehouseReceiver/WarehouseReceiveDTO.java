package com.fanstatic.dto.model.warehouseReceiver;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.supplier.SupplierDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WarehouseReceiveDTO extends ResponseDataDTO {
    private int id;
    private SupplierDTO supplierDTO;

    private String imageFileUrl;

    private String description;


    private boolean active;
}
