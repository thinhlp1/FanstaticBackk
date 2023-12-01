
package com.fanstatic.dto.model.statistical;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSellProductVariantDTO extends ResponseDataDTO{
    private ProductVarientDTO productVariant;
    private long quantity ;
}