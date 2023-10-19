package com.fanstatic.controller.manage;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    @GetMapping("/check-role")
    @ResponseBody
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("OK");
    }
}
