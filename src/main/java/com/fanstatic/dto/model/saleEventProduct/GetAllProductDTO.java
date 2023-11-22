package com.fanstatic.dto.model.saleEventProduct;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.comboProduct.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductForSaleDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.saleevent.SaleEventDTO;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.SaleEvent;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductDTO extends ResponseDataDTO {

    private List<ProductForSaleDTO> products;

     private List<ComboProductDTO> comboProducts;

       private List<ProductVarientDTO> productVarients;
}

