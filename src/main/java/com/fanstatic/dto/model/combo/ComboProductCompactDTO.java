package com.fanstatic.dto.model.combo;

import com.fanstatic.dto.model.saleevent.SaleEventDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductCompactDTO {
    private int id;

    private String code;

    private String name;

    private Long price;

    private String imageUrl;

    private String descriptionUrl;

    private SaleEventDTO saleEvent;
}
