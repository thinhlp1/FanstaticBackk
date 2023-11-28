package com.fanstatic.dto.model.order;

import com.fanstatic.dto.model.extraportion.ExtraPortionDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderExtraPortionDTO {
    private int id;

    private ExtraPortionDTO extraPortion;

    private int quantity;

    private Long total;
}
