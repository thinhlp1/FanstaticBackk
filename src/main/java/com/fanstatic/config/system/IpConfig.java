package com.fanstatic.config.system;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IpConfig extends ResponseDataDTO{
    private String ipAddress;

}
