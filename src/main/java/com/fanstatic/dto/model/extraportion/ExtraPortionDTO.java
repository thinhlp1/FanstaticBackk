package com.fanstatic.dto.model.extraportion;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.category.CategoryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtraPortionDTO extends ResponseDataDTO {
    private int extraPortionId;
    private String code;

    private String name;

    private int price;

    private String type;

    private String imageFileUrl;

    private CategoryDTO categoryDto;

    private boolean active;


}
