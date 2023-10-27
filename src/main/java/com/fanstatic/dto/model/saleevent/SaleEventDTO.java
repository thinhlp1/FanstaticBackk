package com.fanstatic.dto.model.saleevent;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;

@Data
public class SaleEventDTO extends ResponseDataDTO{
    int id;
    double percent;
    String description;
    Date startAt;
    Date endAt;
    Date updateAt;
    Date deleteAt;
    Date createAt;
    int updateBy;
    int deleteBy;
    Boolean active;
}
