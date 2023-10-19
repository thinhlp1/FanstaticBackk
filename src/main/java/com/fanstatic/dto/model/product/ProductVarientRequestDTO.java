package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductVarientRequestDTO extends ResponseDataDTO {
    private int id;

	private String code;

	private BigInteger price;

    private int size;

    
}
