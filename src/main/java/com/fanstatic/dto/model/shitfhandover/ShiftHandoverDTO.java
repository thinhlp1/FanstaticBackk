package com.fanstatic.dto.model.shitfhandover;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.dto.model.user.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShiftHandoverDTO extends ResponseDataDTO {

    private int id;

    private Long cashHandover;

    private Long endShiftCash;

    private Date endShiftTime;

    private String note;

    private Long startShiftCash;

    private Date startShiftTime;

    private UserCompactDTO employee;
}
