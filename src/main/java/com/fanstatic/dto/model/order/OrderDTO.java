package com.fanstatic.dto.model.order;

import java.util.Date;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.bill.BillDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;
import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;
import com.fanstatic.dto.model.payment.PaymentMethodDTO;
import com.fanstatic.dto.model.status.StatusDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.OrderExtraPortion;
import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO extends ResponseDataDTO {
    private int id;

    private String orderType;

    private String note;

    private int people;

    private CustomerDTO customer;

    private Long total;

    private Long point;

    private Long pointRedeem;

    private long finalTotal;

    private Long voucherRedeem;

    private StatusDTO status;

    private VoucherDTO voucher;

    private PaymentMethodDTO paymentMethodDTO;

    private List<OrderItemDTO> orderItems;

    private List<OrderExtraPortionDTO> extraPortions;

    private List<TableDTO> tables;

    private Date createAt;

    private Date updateAt;

    private Date deleteAt;

    private UserCompactDTO emploeeConfirmed;

    private UserCompactDTO canceler;

    private BillDTO bill;
}
