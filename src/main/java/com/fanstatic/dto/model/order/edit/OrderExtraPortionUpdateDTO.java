package com.fanstatic.dto.model.order.edit;

import com.google.firebase.database.annotations.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderExtraPortionUpdateDTO {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer id;

    @NotNull
    private int quantity;
}
