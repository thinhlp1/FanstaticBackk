package com.fanstatic.dto.model.statistical;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardDataDTO  extends ResponseDataDTO{
    private DataDTO listSoldProducts;
    private DataDTO listRevenue;
    private DataDTO listOrders;
    private List<DataSellProductDTO> listTopProduct;
}
