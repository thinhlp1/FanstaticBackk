package com.fanstatic.dto.model.warehouseReceive;

import com.fanstatic.dto.model.warehouseReceiveItem.WarehouseReceiveItemRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class WarehouseReceiveRequestDTO {
    private int id;

    private String code;

    @NotNull(message = "{NotNull.warehouseReceive.supplierId}")
    private int supplierId;

    private Optional<MultipartFile> imageFile;

    private String description;

    private boolean active;

    private String checkOutBy;

    List<WarehouseReceiveItemRequestDTO> warehouseReceiveItemRequestDTOList;
}
