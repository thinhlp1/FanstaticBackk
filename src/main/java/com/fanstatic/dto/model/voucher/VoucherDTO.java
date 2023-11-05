package com.fanstatic.dto.model.voucher;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class VoucherDTO extends ResponseDataDTO {
    private int id;
    private String voucherCode;

    private String name;

    private BigInteger value;

    private int percent;

    private BigInteger priceCondition;

    private Date startAt;

    private Date endAt;

}
