package com.fanstatic.dto.model.order.edit;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompleteOrderItemDTO {

    private Integer orderId;

    private Integer id;

    private Integer quantityCompleted;

}
