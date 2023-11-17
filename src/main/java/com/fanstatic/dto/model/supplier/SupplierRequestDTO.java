package com.fanstatic.dto.model.supplier;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierRequestDTO {
    
    private int id;

    private String name;

    private String number_phone;

    private String address;

}
