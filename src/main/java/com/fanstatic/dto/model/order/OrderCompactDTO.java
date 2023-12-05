package com.fanstatic.dto.model.order;

import java.util.Date;
import java.util.List;

import com.fanstatic.dto.model.bill.BillDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.order.checkout.OrderSurchangeDTO;
import com.fanstatic.dto.model.payment.PaymentMethodDTO;
import com.fanstatic.dto.model.status.StatusDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCompactDTO {
    private int id;

    private String orderType;

    private String note;

    private int people;

    private CustomerDTO customer;

    private Long total;

    private long finalTotal;

    private Long voucherRedeem;

    private Long totalSurchange;

    private Long receiMoney;

    private StatusDTO status;

    private List<OrderItemDTO> orderItems;

    private List<OrderExtraPortionDTO> extraPortions;

    private Date createAt;

    private Date updateAt;

    private Date deleteAt;

}
