package com.fanstatic.dto.model.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductChangeStockDTO {
    private Integer productId;

    private Integer comboProductId;

    private Integer productVariantId;

    private boolean outOfStack;
}
