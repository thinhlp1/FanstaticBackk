package com.fanstatic.dto.model.order.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtraPortionOrderRequestDTO {
    
    private int id;

    private int extraPortionId;

    private int quantity;
}
