package com.fanstatic.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.service.authentication.AuthenticationService;
import com.fanstatic.util.ResponseUtils;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        ResponseDTO reponseDTO = authenticationService.login(loginDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

}
