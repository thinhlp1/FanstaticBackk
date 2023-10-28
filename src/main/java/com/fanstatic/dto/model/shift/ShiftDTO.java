package com.fanstatic.dto.model.shift;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ShiftDTO extends ResponseDataDTO {
    private String id;

    private String shift;

    private String code;

    private Date startAt;

    private Date endAt;

    private boolean active;
}
