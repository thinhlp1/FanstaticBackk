package com.fanstatic.controller.checkouttest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanstatic.service.payos.PayOSService;

@RestController
public class CheckoutTest {
    @Autowired
    private PayOSService payOSService;

    @GetMapping("/checkout")
    public String checkout() {
        return payOSService.getCheckoutUrl(25, 1000L, "Thanh toán hóa đơn").toString();
    }

}
