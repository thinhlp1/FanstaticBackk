package com.fanstatic.dto.model.payment;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentMethodDTO extends ResponseDataDTO {
    private String id;

    private String name;

    private String imageUrl;
}
