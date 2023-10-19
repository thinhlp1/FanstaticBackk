package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import com.fanstatic.dto.model.category.CategoryDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {
    private int id;

    @NotNull(message = "{NotNull.category.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.category.code}")
    private String code;

    @NotBlank(message = "{NotBlank.category.name}")
    @Size(min = 1, max = 50, message = "{Size.category.name}")
    private String name;

    private BigInteger price;

    private List<CategoryDTO> categories;

    private List<ProductVarientDTO> productVarients;

}
