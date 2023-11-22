package com.fanstatic.dto.model.warehouseReceiver;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class WarehouseReceiveRequestDTO {
    private int id;

    @NotNull(message = "{NotNull.warehouseReceive.supplierId}")
    private int supplierId;

    private MultipartFile imageFile;

    private String description;

    private boolean active;
}
