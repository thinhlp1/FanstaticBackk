package com.fanstatic.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.config.system.ContactConfig;
import com.fanstatic.config.system.SystemConfig;
import com.fanstatic.service.system.SystemConfigService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SystemConfigController {
    private final SystemConfigService systemConfigService;

    @GetMapping("/api/u/systemconfig/contact")
    @ResponseBody
    public SystemConfig getContactConfig() {
        return systemConfigService.getConfig();
    }

    @PostMapping("/api/u/systemconfig/contact")
    @ResponseBody
    public void updateContact(@RequestBody SystemConfig config) {
         systemConfigService.updateConfig(config);;
    }
}
