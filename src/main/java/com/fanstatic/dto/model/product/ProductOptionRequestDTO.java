package com.fanstatic.dto.model.product;

import java.util.List;

import com.fanstatic.dto.model.option.OptionGroupRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductOptionRequestDTO {

    private int productId;

    private List<Integer> optionShareds;
    private List<OptionGroupRequestDTO> optionGroups;

}
