package com.fanstatic.dto.model.combo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDetailRequestDTO {

    private Integer id;

    private Integer comboProductId;

    private Integer productId;

    private Integer productVarientId;

    private Integer extraPortionId;

    private Integer quantity;
}
