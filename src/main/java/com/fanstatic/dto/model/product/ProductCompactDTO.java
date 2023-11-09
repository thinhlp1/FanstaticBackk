package com.fanstatic.dto.model.product;

import java.util.List;

import com.fanstatic.dto.model.category.CategoryDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCompactDTO {
    private int id;

    private String code;

    private String name;

    private Long price;

    private List<ProductImageDTO> imageUrl;
}
