package com.fanstatic.dto.model.voucher;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.order.checkout.ApplyVoucherDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VourcherApplyOrderDTO extends ResponseDataDTO{
    private int id;
    private String voucherCode;

    private String name;

    private Long value;

    private int percent;

    // thịnh thêm thuộc tính quantity
    private int quantity;

    // thịnh thêm thuộc tính quantityCollected
    private Long quantityCollected;

    private Long priceCondition;

    private Date startAt;

    private Date endAt;

    private boolean active;

    private ApplyVoucherDTO applyVoucherDTO;
}
