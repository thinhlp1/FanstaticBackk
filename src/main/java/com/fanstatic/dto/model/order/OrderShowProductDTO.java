package com.fanstatic.dto.model.order;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.combo.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderShowProductDTO extends ResponseDataDTO{
    private List<ProductDTO> products;

    private List<ComboProductDTO> comboProducts;
}
