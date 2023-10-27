package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private String name;

    private BigInteger price;

    private byte active;

    private List<CategoryDTO> categories;

    private List<ProductVarientDTO> productVarients;

    private List<String> imageUrl;

    private String descriptionUrl;

}
