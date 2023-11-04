package com.fanstatic.dto.model.unit;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UnitDTO extends ResponseDataDTO {
    private String id;

    private String name;

    private byte active;

    private String description;


    private String sign;
}
