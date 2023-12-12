package com.fanstatic.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.ConfirmOtpDTO;
import com.fanstatic.dto.auth.ConfirmPasswordDTO;
import com.fanstatic.dto.auth.LoginAccountDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.auth.LoginPasswordDTO;
import com.fanstatic.service.authentication.AuthenticationService;
import com.fanstatic.util.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping
    @ResponseBody
     public ResponseEntity<ResponseDTO> checkRolePerssion(@RequestBody String feRoute) {
        // ResponseDTO reponseDTO = authenticationService.login(loginDTO);
        // return ResponseUtils.returnReponsetoClient(reponseDTO);
        return null;
    }
    @PostMapping("/employee/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginAccountDTO loginDTO) {
        ResponseDTO reponseDTO = authenticationService.login(loginDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        ResponseDTO reponseDTO = authenticationService.login(loginDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/login-password")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginPasswordDTO loginDTO) {
        ResponseDTO reponseDTO = authenticationService.loginPassword(loginDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/confirm-otp")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirmOTP(@RequestBody @Valid ConfirmOtpDTO confirmOtpDTO) {
        ResponseDTO reponseDTO = authenticationService.confirmOTP(confirmOtpDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/resend-otp")
    @ResponseBody
    public ResponseEntity<ResponseDTO> resendOTP() {
        ResponseDTO reponseDTO = authenticationService.resendOTP();
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/confirm-password")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirmPassword(@RequestBody @Valid ConfirmPasswordDTO confirmPasswordDTO) {
        ResponseDTO reponseDTO = authenticationService.confirmPassword(confirmPasswordDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @PostMapping("/forget/confirm-password")
    @ResponseBody
    public ResponseEntity<ResponseDTO> forgetConfirmPassword(
            @RequestBody @Valid ConfirmPasswordDTO confirmPasswordDTO) {
        ResponseDTO reponseDTO = authenticationService.forgetConfirmPassword(confirmPasswordDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseDTO reponseDTO = authenticationService.logout(request, response);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
}
