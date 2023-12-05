package com.fanstatic.dto.model.order.edit;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.internal.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePaymentRequestDTO {

    @NotNull
    private Integer orderId;

    @NotNull
    private String paymentMethod;

    private Long receiveMoney;
}
