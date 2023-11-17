package com.fanstatic.dto.model.bill;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.payment.PaymentMethodDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillDTO extends ResponseDataDTO {
    private int billId;

    private Long receiveMoney;

    private String status;

    private PaymentMethodDTO paymentMethod;

    private String checkoutUrl;

}
