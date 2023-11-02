package com.fanstatic.dto.model.table;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableTypeRequestDTO {

    private int id;

    @NotNull(message = "{NotBlank.tabletype.capacity}")
    @Min(value = 1, message = "{Min.tabletype.capacity}")
    private int capacity;

    @NotBlank(message = "{NotBlank.tabletype.name}")
    @Size(min = 1, max = 50, message = "{Size.tabletype.name}")
    private String name;

    @NotNull(message = "{NotNull.tabletype.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.tabletype.code}")
    private String code;

    private MultipartFile image;

}
