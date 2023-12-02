package com.fanstatic.dto.model.option;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionGroupDTO  extends ResponseDataDTO{
    private int id;

    private String name;

    private boolean multichoice;

    private boolean require;

    private List<OptionDTO> options;
}
