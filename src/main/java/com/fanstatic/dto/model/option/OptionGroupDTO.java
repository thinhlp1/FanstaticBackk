package com.fanstatic.dto.model.option;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionGroupDTO {
    private int id;

    private String name;

    private boolean multichoice;

    private List<OptionDTO> options;
}
