package com.fanstatic.dto.model.order;

import java.util.List;

import com.fanstatic.dto.model.category.CategoryCompactDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemSessionDTO {
    private List<OrderItemDTO> orderItems;

    private CategoryCompactDTO categoryCompactDTO;
}
