package com.fanstatic.dto.model.product;

import java.math.BigInteger;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.size.SizeDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductVarientDTO extends ResponseDataDTO{
    private int id;

    private String code;

    private BigInteger price;

    private SizeDTO size;

}
