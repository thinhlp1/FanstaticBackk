package com.fanstatic.dto.model.order.edit;

import java.util.List;

import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderNewItemDTO {
    private Integer orderId;

    private List<OrderItemRequestDTO> orderItems;

    private List<ExtraPortionOrderRequestDTO> extraPortions;
}
