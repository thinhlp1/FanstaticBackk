package com.fanstatic.dto.model.warehouseReceive;

import com.fanstatic.model.WarehouseReceiveItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class WarehouseReceiveRequestDTO {
    private int id;

    @NotNull(message = "{NotNull.warehouseReceive.supplierId}")
    private int supplierId;

    private MultipartFile imageFile;

    private String description;

    private boolean active;
    
    List<WarehouseReceiveItem> warehouseReceiveItemList;
}
