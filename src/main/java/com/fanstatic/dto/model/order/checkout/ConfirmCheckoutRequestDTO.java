package com.fanstatic.dto.model.order.checkout;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfirmCheckoutRequestDTO {
    private int orderId;

    private Long receiveMoney;

    private String paymentMethod;
}
