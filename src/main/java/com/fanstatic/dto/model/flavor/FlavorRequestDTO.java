package com.fanstatic.dto.model.flavor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlavorRequestDTO {
    private int id;

    @NotBlank(message = "{NotBlank.flavor.code}")
    private String code;

    @NotBlank(message = "{NotBlank.flavor.name}")
    private String name;

    private String description;

    @NotBlank(message = "{NotBlank.flavor.unit}")
    private String unitId;

    @NotNull(message = "{NotNull.flavor.flavorCategory}")
    private int flavorCategoryId;
}
