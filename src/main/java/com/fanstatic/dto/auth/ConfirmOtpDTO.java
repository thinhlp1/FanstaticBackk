package com.fanstatic.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConfirmOtpDTO {
    @NotBlank(message = "Vui lòng nhập OTP")
    @Pattern(regexp = "[0-9]{6}", message = "Vui lòng nhập OTP hợp lệ")
    private String otp;
}
