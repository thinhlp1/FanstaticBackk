package com.fanstatic.dto.model.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnitRequestDTO {

    private String id;

    @NotBlank(message = "{NotBlank.unit.name}")
    private String name;

    private byte active;

    private String description;

    @NotBlank(message = "{NotBlank.unit.sign}")
    private String sign;
}
