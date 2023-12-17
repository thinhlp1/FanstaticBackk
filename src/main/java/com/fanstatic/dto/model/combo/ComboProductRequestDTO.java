package com.fanstatic.dto.model.combo;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComboProductRequestDTO {
    private int id;

    @NotNull(message = "{NotNull.product.code}")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.product.code}")
    private String code;

    @NotBlank(message = "{NotBlank.product.name}")
    @Size(min = 1, max = 50, message = "{Size.product.name}")
    private String name;

    @NotNull(message = "{NotNull.product.price}")
    private Long price;

    private Integer categoryId;

    private MultipartFile image;

    private MultipartFile description;

    private List<ComboProductDetailRequestDTO> comboProductDetails;

}
