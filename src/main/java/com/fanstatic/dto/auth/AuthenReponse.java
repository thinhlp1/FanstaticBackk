package com.fanstatic.dto.auth;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenReponse extends ResponseDataDTO {
    private String token;

    private String tokenPermission;

    private boolean needStartShift;

    private RoleDTO roleDTO;
}
