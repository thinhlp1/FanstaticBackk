
package com.fanstatic.dto.model.statistical;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.comboProduct.ComboProductDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSellComboProductDTO extends ResponseDataDTO{
    private ComboProductDTO comboProduct;
    private long quantity ;
}