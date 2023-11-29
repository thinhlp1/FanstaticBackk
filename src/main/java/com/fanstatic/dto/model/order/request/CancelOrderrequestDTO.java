package com.fanstatic.dto.model.order.request;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelOrderrequestDTO {

    @NotNull
    private Integer orderId;

    @NotNull
    private Integer cancelId;
}
