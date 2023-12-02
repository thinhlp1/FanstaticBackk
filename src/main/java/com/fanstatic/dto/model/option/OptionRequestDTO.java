package com.fanstatic.dto.model.option;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequestDTO {
    public OptionRequestDTO(OptionRequestDTO other) {
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        // Sao chép các trường khác cần thiết
    }

    private Integer id;

    private String name;

    private Long price;
}
