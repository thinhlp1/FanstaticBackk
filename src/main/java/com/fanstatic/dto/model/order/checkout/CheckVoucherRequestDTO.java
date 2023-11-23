package com.fanstatic.dto.model.order.checkout;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckVoucherRequestDTO {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer voucherId;
}
