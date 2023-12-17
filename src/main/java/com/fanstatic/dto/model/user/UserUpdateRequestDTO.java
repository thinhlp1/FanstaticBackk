package com.fanstatic.dto.model.user;


import java.math.BigInteger;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequestDTO {

    private int id;

    @NotNull
    private Date dateOfBirth;

    @NotBlank(message = "{NotBlank.user.email}")
    @Email(message = "{Email.user.email}")
    private String email;

    @NotNull
    private byte gender;

    @NotEmpty(message = "{NotEmpty.user.cccdCmnd}")
    @Size(min = 12, max = 12, message = "{Size.user.cccdCmnd}")
    private String cccdCmnd;

    @NotBlank(message = "{NotBlank.user.name}")
    @Size(min = 1, max = 50, message = "{Size.user.name}")
    private String name;

    @Pattern(regexp = "^(03|05|07|09)\\d{8}$", message = "{Pattern.user.numberPhone}")
    @NotBlank(message = "{NotBlank.user.numberPhone}")
    private String numberPhone;

    @NotEmpty(message = "{NotEmpty.user.placeOrigin}")
    @Size(min = 1, max = 100, message = "{Size.user.placeOrigin}")
    private String placeOrigin;

    @NotEmpty(message = "{NotEmpty.user.placeResidence}")
    @Size(min = 1, max = 100, message = "{Size.user.placeResidence}")
    private String placeResidence;

    private BigInteger point;

    private int roleId;


    public String getNumberPhone() {
        return this.numberPhone;
    }

    private MultipartFile image;

}
