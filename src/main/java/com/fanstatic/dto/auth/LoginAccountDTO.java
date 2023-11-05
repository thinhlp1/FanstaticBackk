package com.fanstatic.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginAccountDTO {
    private String numberPhone;
    private String password;

    public String getUsername(){
        return numberPhone;
    }
}
