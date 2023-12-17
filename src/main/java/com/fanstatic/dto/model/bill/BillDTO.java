package com.fanstatic.dto.model.bill;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.payment.PaymentMethodDTO;
import com.fanstatic.dto.model.status.StatusDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillDTO extends ResponseDataDTO {
    private int billId;

    private Long receiveMoney;

    private StatusDTO status;

    private PaymentMethodDTO paymentMethod;

    private String checkoutUrl;

    private Date updateAt;

    private Date createAt;

    private Date deleteAt;

}
