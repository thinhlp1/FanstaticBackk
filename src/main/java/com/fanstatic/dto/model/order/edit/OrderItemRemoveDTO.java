package com.fanstatic.dto.model.order.edit;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRemoveDTO {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer id;
}
