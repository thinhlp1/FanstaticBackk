package com.fanstatic.config.system;

import java.util.Date;
import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig extends ResponseDataDTO{
    private OrderConfig allowOrder;
    private ContactConfig contact;
    private PointProgramConfig pointProgram;
    private List<IpConfig> ipConfigs;


}
