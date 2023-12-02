package com.fanstatic.dto.model.product;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.option.OptionGroupDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private String name;

    private Long price;

    private boolean active;

    private boolean outOfStock;

    private int soldQuantity;

    private boolean hotProduct;

    private List<CategoryDTO> categories;

    private List<ProductVarientDTO> productVarients;

    private List<ProductImageDTO> imageUrl;

    private String descriptionUrl;

    private SaleEventDTO saleEvent;

    private List<OptionGroupDTO> optionGroups;

   private Long varientPrice;
}
