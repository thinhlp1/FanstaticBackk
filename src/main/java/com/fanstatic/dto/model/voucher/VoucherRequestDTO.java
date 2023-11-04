package com.fanstatic.dto.model.voucher;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class VoucherRequestDTO {
    private int id;

    @NotBlank(message = "{NotBlank.voucher.voucherCode}")
    private String voucherCode;

    @NotBlank(message = "{NotBlank.voucher.name}")
    private String name;

    @NotNull(message = "{NotNull.voucher.value}")
    @Min(value = 1000, message = "{Min.voucher.value}")
    @Max(value = 2000000000, message = "{Max.voucher.value}")
    private BigInteger value;

    @NotNull(message = "{NotNull.voucher.percent}")
    @Min(value = 1, message = "{Min.voucher.percent}")
    @Max(value = 100, message = "{Max.voucher.percent}")
    private int percent;

    @NotNull(message = "{NotNull.voucher.priceCondition}")
    private BigInteger priceCondition;

    private Date startAt;

    @Future(message = "{Future.voucher.endAt}")
    private Date endAt;
}
