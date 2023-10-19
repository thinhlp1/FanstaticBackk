package com.fanstatic.dto.auth;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenReponse extends ResponseDataDTO{
    private String token;
}
