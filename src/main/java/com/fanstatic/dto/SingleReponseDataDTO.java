package com.fanstatic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleReponseDataDTO extends ResponseDataDTO {
    private Object data;
}
