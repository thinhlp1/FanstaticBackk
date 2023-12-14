package com.fanstatic.config.system;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointProgramConfig extends ResponseDataDTO {
    private Long minPrice;
    private Long maxPoint;
    private ConvertRate moneyToPoint;
    private ConvertRate pointToMoney;

}

