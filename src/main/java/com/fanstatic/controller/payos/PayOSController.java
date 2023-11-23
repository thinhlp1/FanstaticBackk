package com.fanstatic.controller.payos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanstatic.service.payos.PayOSService;

@RestController
public class PayOSController {

    @Autowired
    private PayOSService payOS;

    @PostMapping("auth/payos/checkout")
    public String checkout() {
        return payOS.getCheckoutUrl(10000635, Long.valueOf(800000), "test");
    }
}
