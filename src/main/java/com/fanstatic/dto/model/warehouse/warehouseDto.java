package com.fanstatic.dto.model.warehouse;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class warehouseDto extends ResponseDataDTO {
    private int id;

    private String flavorName;

    private long flavorQuantity;


}
