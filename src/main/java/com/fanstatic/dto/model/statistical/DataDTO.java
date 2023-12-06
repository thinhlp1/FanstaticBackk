package com.fanstatic.dto.model.statistical;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO extends ResponseDataDTO{
    private Object dataToday;
    private Object dataMonth;
    private Object dataWeek;

    public DataDTO (Object dataToday, Object dataWeek, Object dataMonth){
        this.dataToday = dataToday;
        this.dataWeek = dataWeek;
        this.dataMonth = dataMonth;
    }

    private Object[] dataMonths = new Object[12];

    public void setDataMonth(int monthIndex, Object data) {
        monthIndex--;
        if (monthIndex >= 0 && monthIndex < 12) {
            dataMonths[monthIndex] = data;
        } else {
            throw new IllegalArgumentException("Invalid month index: " + monthIndex);
        }
    }

    public Object getDataMonth(int monthIndex) {
            monthIndex--;
        if (monthIndex >= 0 && monthIndex < 12) {
            return dataMonths[monthIndex];
        } else {
            throw new IllegalArgumentException("Invalid month index: " + monthIndex);
        }
    }
}
