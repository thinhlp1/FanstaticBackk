package com.fanstatic.config.system;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderConfig extends ResponseDataDTO{

    private boolean tableMix;
    private boolean surcharge;

}
