package com.fanstatic.dto.model.shift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShiftRequestDTO {

    private int id;

    @NotNull(message = "{NotNull.shift.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.shift.code}")
    private String code;

    @NotBlank(message = "{NotBlank.shift.shift}")
    private String shift;

    @NotBlank(message = "{NotBlank.shift.startAt}")
    private String startAt;

    @NotBlank(message = "{NotBlank.shift.endAt}")
    private String endAt;
}
