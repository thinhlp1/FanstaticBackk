package com.fanstatic.dto.model.order.checkout;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyVoucherDTO extends ResponseDataDTO{

    private long total;

    private long discount;

    private long finalTotal;
}
