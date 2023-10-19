package com.fanstatic.dto.auth;

import lombok.Data;

@Data
public class CreateAccountDTO {
    private String numberPhone;
    private String password;
    private int roleId;
}
