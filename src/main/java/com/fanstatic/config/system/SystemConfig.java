package com.fanstatic.config.system;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SystemConfig {
    private List<OrderConfig> allowOrder;
    private List<ContactConfig> contact;
    private List<PointProgramConfig> pointProgram;
    private List<IpConfig> ipConfig;

    public SystemConfig() {

    }

    public SystemConfig(String jsonString) {

    }
}
