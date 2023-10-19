package com.fanstatic.dto.model.product;

import java.math.BigInteger;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @EqualsAndHashCode(callSuper=false)
public class ProductVarientDTO extends ResponseDataDTO{
    private int id;

    private String code;

    private BigInteger price;

    private int size;

}
