package com.fanstatic.dto.model.order.request;

import java.util.List;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequestDTO {

    private int id;

    @NotNull
    private int orderType;

    @NotNull
    private String note;

    @NotNull
    private int tableId;

    @NotNull
    private int people;

    private List<ExtraPortionOrderRequestDTO> extraPortions;

    private List<OrderItemRequestDTO> orderItems;

}
