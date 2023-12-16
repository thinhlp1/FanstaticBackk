package com.fanstatic.dto.model.comboProduct;

import com.fanstatic.dto.ResponseDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductDTO extends ResponseDataDTO {
    private int id;

    private String name;

    private int price;
}
