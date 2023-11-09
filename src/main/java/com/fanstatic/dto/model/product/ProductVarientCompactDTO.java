package com.fanstatic.dto.model.product;

import java.util.List;

import com.fanstatic.dto.model.size.SizeDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductVarientCompactDTO {
    private int id;

    private String code;

    private Long price;

    private SizeDTO size;

    private List<ProductImageDTO> imageUrl;

}
