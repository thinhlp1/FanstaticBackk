package com.fanstatic.dto.model.voucher;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO extends ResponseDataDTO {
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
}
