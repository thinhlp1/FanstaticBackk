package com.fanstatic.dto.model.saleEventProduct;


import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleProductRequestDTO {
    
    private int id;

    private List<Integer> product;

    private List<Integer>  productVarient;

    private List<Integer> comboProduct;

    private int saleEvent;

   

}
