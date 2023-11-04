package com.fanstatic.dto.auth;

import lombok.Data;

@Data
public class ConfirmPasswordDTO {
    
    private String password;

    private String confirmPassword;
}
