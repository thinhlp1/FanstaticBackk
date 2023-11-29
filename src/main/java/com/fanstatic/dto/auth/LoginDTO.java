package com.fanstatic.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Vui lòng nhập số điện thoại")
    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String numberPhone;

    public String getUsername() {
        return numberPhone;
    }
}
