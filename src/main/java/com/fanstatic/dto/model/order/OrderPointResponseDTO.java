package com.fanstatic.dto.model.order;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class OrderPointResponseDTO extends ResponseDataDTO {
    // private boolean canRedeem;
    private Long minPrice;

    private Long point;

    private Long moneyCanReem;

    private Long pointLeft;
}
