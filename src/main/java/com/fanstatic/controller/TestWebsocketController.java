package com.fanstatic.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.account.AccountDTO;
import com.fanstatic.util.ResponseUtils;

@Controller
@CrossOrigin("http://localhost:5500/")
public class TestWebsocketController {
    
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ResponseDTO greet(String message) throws InterruptedException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumberPhone("0334831013");
        accountDTO.setPassword("12082003az9");
        return ResponseUtils.success(200, "OKE", null);
    }
}
