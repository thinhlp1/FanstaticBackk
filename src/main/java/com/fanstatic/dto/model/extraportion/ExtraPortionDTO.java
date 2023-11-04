package com.fanstatic.dto.model.extraportion;

import com.fanstatic.dto.ResponseDataDTO;
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

    private Integer imageId;

    private Integer categoryId;

    private boolean active;


}
