package com.fanstatic.dto.model.order;

import java.util.List;

import com.fanstatic.dto.model.comboproduct.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductCompactDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductVarientCompactDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private int id;

    private ProductCompactDTO product;

    private ProductVarientCompactDTO productVarient;

    private ComboProductDTO comboProduct;

    private String note;

    private int quantity;

    private boolean priority;

    private Long total;

    private List<OptionDTO> options;
    
}
