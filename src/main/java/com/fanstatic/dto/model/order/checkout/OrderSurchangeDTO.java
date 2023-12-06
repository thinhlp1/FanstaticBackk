package com.fanstatic.dto.model.order.checkout;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderSurchangeDTO {
    private Integer id;

    private String content;

    private Long price;

}
