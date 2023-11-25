package com.fanstatic.dto.model.requestStaff;

import java.util.Date;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfirmRequestStaffDTO extends ResponseDTO{
    private UserCompactDTO employeeConfirm;

    private Date confirmAt;
}
