package com.fanstatic.dto.model.combo;

import java.util.List;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryCompactDTO;
import com.fanstatic.dto.model.product.ProductImageDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private String name;

    private Long price;

    private String imageUrl;

    private String descriptionUrl;

    private SaleEventDTO saleEvent;

    private CategoryCompactDTO category;

    private boolean active;

    private boolean outOfStock;

    private boolean hotProduct;

    private Integer soldQuantity;

    private List<ComboProductDetailDTO> comboProductDetails;

}
