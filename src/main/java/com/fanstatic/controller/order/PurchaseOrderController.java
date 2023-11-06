package com.fanstatic.controller.order;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.model.account.AccountDTO;
import com.fanstatic.util.ResponseUtils;

@Controller
public class PurchaseOrderController {

    @MessageMapping("/order/create")
    @SendTo("/topic/order/create")
    public ResponseDTO greet(LoginDTO loginDTO) throws InterruptedException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumberPhone("0334831013");
        accountDTO.setPassword("12082003az9");
        System.err.println("CREAETE");
        return ResponseUtils.success(200, "TẠO ORDER", accountDTO);
    }

    @MessageMapping("/order/update")
    @SendTo("/topic/order/update")
    public ResponseDTO greet2(LoginDTO loginDTO) throws InterruptedException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumberPhone("0334831013");
        accountDTO.setPassword("12082003az9");
        System.err.println("UPDATE");

        return ResponseUtils.success(200, "UPDATE ORDER", accountDTO);
    }

    @MessageMapping("/order/delete")
    @SendTo("/topic/order/delete")
    public ResponseDTO greet3(LoginDTO loginDTO) throws InterruptedException {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumberPhone("0334831013");
        accountDTO.setPassword("12082003az9");
        System.err.println("DELETE");

        return ResponseUtils.success(200, "XÓA ORDER", accountDTO);
    }
}

