package com.fanstatic.dto.model.order;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.table.TableDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderTableDTO extends ResponseDataDTO{
    private TableDTO tableDTO;

    private OrderDTO orderDTO;
}
