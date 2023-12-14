package com.fanstatic.dto.model.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class warehouseRequestDto {

    private String flavorName;

    private long flavorQuantity;
}
