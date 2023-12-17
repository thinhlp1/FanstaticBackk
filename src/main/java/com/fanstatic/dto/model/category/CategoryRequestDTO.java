package com.fanstatic.dto.model.category;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {

    private int id;

    @NotNull(message = "{NotNull.category.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.category.code}")
    private String code;

    @NotBlank(message = "{NotBlank.category.name}")
    @Size(min = 1, max = 50, message = "{Size.category.name}")
    private String name;

    @Digits(integer = 10, fraction = 0, message = "{Digits.category.level}")
    private Integer parent_id;

    private MultipartFile imageFile;
}
