package com.fanstatic.dto.model.saleEventProduct;

import com.fanstatic.model.ComboProduct;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.comboProduct.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleProductRequestDTO {
    
    private int id;

    private ProductDTO product;

    private ProductVarientDTO productVarient;

    private SaleEventDTO saleEvent;

    private ComboProductDTO comboProduct;

    private byte active;
}
