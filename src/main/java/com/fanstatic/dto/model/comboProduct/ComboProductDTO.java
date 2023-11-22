package com.fanstatic.dto.model.comboProduct;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.saleEventProduct.ObjectSaleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDTO extends ResponseDataDTO implements ObjectSaleDTO {
    private int id;

    private String name;
}
