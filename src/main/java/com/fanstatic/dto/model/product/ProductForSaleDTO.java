package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.saleEventProduct.ObjectSaleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductForSaleDTO extends ResponseDataDTO implements ObjectSaleDTO {
    private int id;

    private String code;

    private String name;

    private BigInteger price;

    private byte active;

    private List<ProductVarientDTO> productVarients;

    private String descriptionUrl;

}
