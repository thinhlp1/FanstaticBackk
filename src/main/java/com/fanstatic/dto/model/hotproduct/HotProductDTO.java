package com.fanstatic.dto.model.hotproduct;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.combo.ComboProductCompactDTO;
import com.fanstatic.dto.model.product.ProductCompactDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.google.cloud.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotProductDTO extends ResponseDataDTO {
    private int id;

    private ProductDTO product;

    private ComboProductCompactDTO comboProduct;

    private Date startAt;

    private Date endAt;

    private Integer serial;
}
