package com.fanstatic.dto.model.combo;
import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;
import com.fanstatic.dto.model.product.ProductCompactDTO;
import com.fanstatic.dto.model.product.ProductVarientCompactDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDetailDTO {
    private Integer id;

    private ExtraPortionDTO extraPortion;

    private ProductCompactDTO product;

    private ProductVarientCompactDTO productVarient;

    private Integer quantity;
}
