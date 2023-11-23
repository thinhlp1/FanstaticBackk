package com.fanstatic.config.system;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointProgramConfig extends ResponseDataDTO {
    private Long minPoint;
    private Long maxPoint;
    private ConvertRate convertRate;
    private String startAt;
    private String endAt;

}

@Data
class ConvertRate  {
    private Long from;
    private Long to;
    private String currency;

}