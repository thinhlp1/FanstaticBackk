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
        return payOS.getCheckoutUrl(100008123, Long.valueOf(1000), "test");
    }

    @PostMapping("auth/payos/checkout11")
    public String checkout11() {
        return payOS.getCheckoutUrl(100008123, Long.valueOf(1000), "test");
    }

    @PostMapping("auth/payos/checkout2")
    public String checkout2() {
        return payOS.getCheckoutUrl(100009123, Long.valueOf(1000), "test");
    }

    @PostMapping("auth/payos/checkout22")
    public String checkout22() {
        return payOS.getCheckoutUrl(100009123, Long.valueOf(1000), "test");
    }

    @PostMapping("auth/payos/checkout3")
    public String checkout3() {
        return payOS.getCheckoutUrl(1000010123, Long.valueOf(1000), "test");
    }

    @PostMapping("auth/payos/checkout33")
    public String checkout33() {
        return payOS.getCheckoutUrl(1000010123, Long.valueOf(1000), "test");
    }
}
