package com.fanstatic.dto.auth;

import lombok.Data;

@Data
public class LoginDTO {
    private String numberPhone;
    private String password;

    public String getUsername(){
        return numberPhone;
    }
}
