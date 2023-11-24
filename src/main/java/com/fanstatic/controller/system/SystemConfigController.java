package com.fanstatic.controller.system;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fanstatic.config.system.ContactConfig;
import com.fanstatic.config.system.IpConfig;
import com.fanstatic.config.system.PointProgramConfig;
import com.fanstatic.config.system.SystemConfig;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.service.system.SystemConfigService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SystemConfigController {
    private final SystemConfigService systemConfigService;

    @GetMapping("/api/manage/systemconfig/show/config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getConfig() {
        return ResponseUtils.returnReponsetoClient(systemConfigService.getConfig());
    }

    @PostMapping("/api/manage/systemconfig/update/config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateConfig(@RequestBody SystemConfig config) {

        return ResponseUtils.returnReponsetoClient(systemConfigService.updateConfig(config));

    }

    @GetMapping("/api/manage/systemconfig/show/ip-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getIpConfig() {
        return ResponseUtils.returnReponsetoClient(systemConfigService.getConfig());

    }

    @PostMapping("/api/manage/systemconfig/update/ip-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateIpConfig(@RequestBody IpConfig config) {
        return ResponseUtils.returnReponsetoClient(systemConfigService.updateIpConfig(config));
    }

    @GetMapping("/api/manage/systemconfig/show/contact-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getContactConfig() {
        return ResponseUtils.returnReponsetoClient(systemConfigService.getContactConfig());

    }

    @PostMapping("/api/manage/systemconfig/update/contact-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateConfig(@RequestBody ContactConfig config) {

        return ResponseUtils.returnReponsetoClient(systemConfigService.updateContactConfig(config));

    }

    @GetMapping("/api/manage/systemconfig/show/point-program-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getPointProgram() {
        return ResponseUtils.returnReponsetoClient(systemConfigService.getPointProgramConfig());
    }

    @PostMapping("/api/manage/systemconfig/update/point-program-config")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updatePointProgramConfig(@RequestBody PointProgramConfig config) {
        return ResponseUtils.returnReponsetoClient(systemConfigService.updatePointProgram(config));
        
    }
}
