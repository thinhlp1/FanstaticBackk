package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.account.AccountRequestDTO;
import com.fanstatic.service.model.AccountService;
import com.fanstatic.util.ResponseUtils;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        ResponseDTO reponseDTO = accountService.create(accountRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }
    
}
