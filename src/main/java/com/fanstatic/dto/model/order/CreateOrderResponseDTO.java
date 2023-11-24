package com.fanstatic.dto.model.order;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponseDTO extends ResponseDataDTO{
    private Integer orderId;

    private Integer customerId;

    private boolean needConfirm;
}
