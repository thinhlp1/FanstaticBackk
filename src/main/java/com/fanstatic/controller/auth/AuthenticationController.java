package com.fanstatic.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.service.authentication.AuthenticationService;
import com.fanstatic.util.ResponseUtils;

import lombok.AllArgsConstructor;

@Controller
@CrossOrigin("http://localhost:3000")
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
