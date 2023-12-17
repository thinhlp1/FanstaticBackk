package com.fanstatic.dto.model.option;

import com.fanstatic.dto.model.product.ProductDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductOptionDTO {
    private int id;

    private OptionGroupDTO optionGroup;

    private ProductDTO product;
}
