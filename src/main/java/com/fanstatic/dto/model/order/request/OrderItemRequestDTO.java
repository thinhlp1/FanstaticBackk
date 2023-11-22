package com.fanstatic.dto.model.order.request;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequestDTO {

    private int orderId;

    private int id;

    private Integer productId;

    private Integer productVariantId;

    private Integer comboId;

    private String note;

    private int quantity;

    private boolean priority;

    private List<Integer> optionsId;

}
