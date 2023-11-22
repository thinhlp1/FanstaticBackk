package com.fanstatic.dto.model.order.checkout;

import com.google.firebase.database.annotations.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutRequestDTO {

    @NotNull
    @NotBlank
    private String paymentMethod;

    @NotNull
    private Integer orderId;

    @NotNull
    private Integer voucherId;
}
