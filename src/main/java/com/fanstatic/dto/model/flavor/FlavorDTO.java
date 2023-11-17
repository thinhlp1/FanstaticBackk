package com.fanstatic.dto.model.flavor;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import com.fanstatic.dto.model.unit.UnitDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlavorDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private String name;

    private String description;

    private UnitDTO unitDTO;

    private CategoryDTO categoryDTO;

    private boolean active;
}
