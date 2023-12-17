package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductVarientRequestDTO extends ResponseDataDTO {
    private int id;

    private int productId;

    @NotNull
    @NotNull(message = "{NotNull.productVarient.price}")
    private Long price;

    @NotNull(message = "{NotNull.productVarient.size}")
    private int size;

    private boolean defaultSize;

}
