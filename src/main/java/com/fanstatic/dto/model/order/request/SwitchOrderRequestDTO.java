package com.fanstatic.dto.model.order.request;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SwitchOrderRequestDTO {
    @NotNull
    private Integer destinationTable;

    @NotNull
    private Integer orderId;
}
