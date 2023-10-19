package com.fanstatic.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserCheckController {

    @GetMapping("/check-role")
    @ResponseBody
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("OK");
    }
}