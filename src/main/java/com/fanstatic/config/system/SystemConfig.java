package com.fanstatic.config.system;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SystemConfig {
    private List<OrderConfig> allowOrder;
    private List<ContactConfig> contact;
    private List<PointProgramConfig> pointProgram;
    private List<IpConfig> ipConfig;

  
   

  

  

}
