package com.fanstatic.dto.model.product;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.dto.model.size.SizeDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductVarientDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private Long price;

    private SizeDTO size;

    private List<ProductImageDTO> imageUrl;
    
    private SaleEventDTO saleEvent;

}
