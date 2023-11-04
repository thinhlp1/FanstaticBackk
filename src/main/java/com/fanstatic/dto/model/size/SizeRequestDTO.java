package com.fanstatic.dto.model.size;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeRequestDTO {

    private int id;

    @NotNull(message = "{NotNull.product.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.product.code}")
    private String code;
    
    @NotBlank(message = "{NotBlank.product.name}")
    @Size(min = 1, max = 50, message = "{Size.product.name}")
    private String name;
}
