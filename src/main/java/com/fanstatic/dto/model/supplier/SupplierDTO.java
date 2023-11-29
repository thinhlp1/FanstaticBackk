package com.fanstatic.dto.model.supplier;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierDTO extends ResponseDataDTO {

    private int id;

    private String name;

    private String phone;

    private String address;

    private byte active;
}
