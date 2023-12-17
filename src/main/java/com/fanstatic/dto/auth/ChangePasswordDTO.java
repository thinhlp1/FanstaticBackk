package com.fanstatic.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordDTO {
    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$", message = "Vui lòng nhập mật khẩu đúng định dạng")
    private String oldPassword;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$", message = "Vui lòng nhập mật khẩu đúng định dạng")
    private String password;

    @NotEmpty(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])([A-Za-z\\d@#$%^&+=!]){8,}$", message = "Vui lòng nhập mật khẩu đúng định dạng")
    private String confirmPassword;
}
