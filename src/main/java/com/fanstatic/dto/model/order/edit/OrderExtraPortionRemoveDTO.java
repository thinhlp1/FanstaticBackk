package com.fanstatic.dto.model.order.edit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderExtraPortionRemoveDTO {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer id;
}
