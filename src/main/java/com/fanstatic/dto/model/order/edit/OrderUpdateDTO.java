package com.fanstatic.dto.model.order.edit;

import java.util.List;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderUpdateDTO {

    @NotNull
    private Integer orderId;

    private List<OrderItemUpdateDTO> orderItemUpdates;

    private List<OrderExtraPortionUpdateDTO> orderExtraPortionUpdates;

    private List<Integer> orderItemRemoves;

    private List<Integer> orderExtraPortionRemoves;
}
