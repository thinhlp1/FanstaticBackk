package com.fanstatic.dto.model.flavorcategory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlavorCategoryRequestDTO {
    private int id;

    @NotBlank(message = "{NotBlank.flavorcategory.code}")
    private String code;

    @NotBlank(message = "{NotBlank.flavorcategory.name}")
    private String name;

}
