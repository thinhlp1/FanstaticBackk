package com.fanstatic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/manage")
@AllArgsConstructor
public class TestController {

    @GetMapping("/product/create")
    @ResponseBody
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("OK");
    }
}