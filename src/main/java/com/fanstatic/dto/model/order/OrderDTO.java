package com.fanstatic.dto.model.order;

import java.util.List;

import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;
import com.fanstatic.dto.model.order.request.ExtraPortionOrderRequestDTO;
import com.fanstatic.dto.model.order.request.OrderItemRequestDTO;
import com.fanstatic.dto.model.table.TableDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.ExtraPortion;
import com.fanstatic.model.OrderExtraPortion;
import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    private int id;

    private String orderType;

    private String note;

    private TableDTO table;

    private int people;

    private CustomerDTO customer;

    private long total;

    private String status;

    private VoucherDTO voucher;

    private List<OrderExtraPortionDTO> extraPortions;

    private List<OrderItemDTO> orderItems;
}
