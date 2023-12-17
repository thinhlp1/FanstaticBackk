package com.fanstatic.dto.model.customer;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
public class CustomerRequestDTO {

    private int id;

    @NotNull
    private Date dateOfBirth;

    @NotBlank(message = "{NotBlank.user.email}")
    @Email(message = "{Email.user.email}")
    private String email;

    @NotBlank(message = "{NotBlank.user.name}")
    @Size(min = 1, max = 50, message = "{Size.user.name}")
    private String name;

    @Getter
    @Pattern(regexp = "^(03|05|07|09)\\d{8}$", message = "{Pattern.user.numberPhone}")
    @NotBlank(message = "{NotBlank.user.numberPhone}")
    private String numberPhone;

    private BigInteger point;

    // password for create account
    @NotEmpty(message = "{NotEmpty.user.password}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$", message = "{Pattern.user.password}")
    private String password;

    private MultipartFile image;

}
