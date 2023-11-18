package com.fanstatic.dto.model.flavorcategory;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlavorCategoryDTO extends ResponseDataDTO {
    private int id;

    private String code;

    private String name;

    private boolean active;
}
