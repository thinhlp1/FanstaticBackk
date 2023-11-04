package com.fanstatic.dto.model.saleevent;

import com.fanstatic.dto.ResponseDataDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SaleEventRequestDTO extends ResponseDataDTO {
    private int id;

    @NotNull(message = "NotNull.saleevent.percent")
    @Min(value = 1 , message = "Min.saleevent.percent")
    private double percent;

    private String description;

    private Date startAt;

    private Date endAt;

    private boolean active;

}
