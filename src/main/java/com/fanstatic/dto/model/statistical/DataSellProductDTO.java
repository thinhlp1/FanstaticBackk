package com.fanstatic.dto.model.statistical;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.product.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSellProductDTO extends ResponseDataDTO{
    private ProductDTO product;
    private long quantity ;
}