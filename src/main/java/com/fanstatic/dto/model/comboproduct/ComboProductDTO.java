package com.fanstatic.dto.model.comboproduct;

import java.util.List;

import com.fanstatic.dto.model.product.ProductImageDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDTO {
    private int id;

    private String code;

    private String name;

    private Long price;

    private List<ProductImageDTO> imageUrl;
}
