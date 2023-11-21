package com.fanstatic.dto.model.option;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionGroupRequestDTO {

    public OptionGroupRequestDTO(OptionGroupRequestDTO other) {
        this.id = other.id;
        this.name = other.name;
        this.shared = other.shared;
        this.multichoice = other.multichoice;
        this.options = other.options;
        // Sao chép các trường khác cần thiết
    }

    private Integer id;

    private String name;

    private boolean shared;

    private boolean multichoice;

    private List<OptionRequestDTO> options;
}
