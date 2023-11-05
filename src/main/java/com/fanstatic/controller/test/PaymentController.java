package com.fanstatic.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanstatic.service.payos.PayOS;

@RestController
public class PaymentController {
    @Autowired
    private PayOS payOS;
    @GetMapping("/payment")
    public ResponseEntity<String> payment() {
        return ResponseEntity.ok(payOS.transaction(4, 1000L, "test").toString());
    }
}
