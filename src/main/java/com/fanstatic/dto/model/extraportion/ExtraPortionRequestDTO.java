package com.fanstatic.dto.model.extraportion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtraPortionRequestDTO {
    private int extraPortionId;
    @NotBlank(message = "{NotBlank.extraPortion.code}")
    private String code;

    @NotBlank(message = "{NotBlank.extraPortion.name}")
    private String name;

    @Min(value = 1000, message = "{Min.extraPortion.price}")
    @Max(value = 2000000000, message = "{Max.extraPortion.price}")
    @NotNull(message = "{NotNull.extraPortion.price}")
    private int price;

    private String type;

    @NotNull(message = "{NotNull.extraPortion.imageId}")
    private Integer imageId;

    @NotNull(message = "{NotNull.extraPortion.categoryId}")
    private Integer categoryId;

    private boolean active;
}
