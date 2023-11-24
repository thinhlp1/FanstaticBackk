package com.fanstatic.dto.model.statistical;


import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalRevenueDTO extends ResponseDataDTO{
    List<Object> listdataRevenueByMonths;
    List<Integer> listYear;
}
