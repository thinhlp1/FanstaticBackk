package com.fanstatic.dto.model.saleevent;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleEventDTO extends ResponseDataDTO {
    private int id;

    private double percent;

    private String code;

    private String description;

    private String name;

    private Date startAt;

    private Date endAt;

    private byte active;

}
