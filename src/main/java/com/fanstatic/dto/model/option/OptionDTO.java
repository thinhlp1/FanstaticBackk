package com.fanstatic.dto.model.option;

import com.fanstatic.model.OptionGroup;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionDTO {
    private int id;

    private String name;

    private Long price;

}
