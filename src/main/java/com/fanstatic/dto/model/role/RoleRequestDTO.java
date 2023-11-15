package com.fanstatic.dto.model.role;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleRequestDTO {

    private int id;

    @Size(min = 5, max = 50, message = "{Size.role.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.role.code}")
    @NotBlank(message = "{NotBlank.role.code}")
    private String code;

    @NotBlank(message = "{NotBlank.role.name}")
    @Size(min = 5, max = 50, message = "{Size.role.name}")
    private String name;

    @NotBlank(message = "{NotBlank.role.description}")
    @Size(min = 5, max = 50, message = "{Size.role.description}")
    private String description;


}
