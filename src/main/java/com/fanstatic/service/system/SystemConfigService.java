package com.fanstatic.service.system;

import java.io.File;
import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fanstatic.config.system.SystemConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SystemConfigService {
    private final ObjectMapper objectMapper;

    private SystemConfig systemConfig;

    public SystemConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadConfig();
    }

    public SystemConfig getConfig() {
        System.out.println(systemConfig.toString());
        return systemConfig;
    }

    public void updateConfig(SystemConfig newConfig) {
        this.systemConfig = newConfig;
        saveConfig();
    }

    private void loadConfig() {
        try {
            File configFile = new ClassPathResource("config/config.json").getFile();
            systemConfig = objectMapper.readValue(configFile, SystemConfig.class);
        } catch (IOException e) {
            // Xử lý lỗi khi không thể đọc cấu hình
            e.printStackTrace();
            systemConfig = new SystemConfig(); // Hoặc có thể quyết định xử lý khác tùy thuộc vào yêu cầu
        }
    }

    private void saveConfig() {
        try {
            File configFile = new ClassPathResource("config/config.json").getFile();
            objectMapper.writeValue(configFile, SystemConfig.class);
        } catch (IOException e) {
            // Xử lý lỗi khi không thể ghi cấu hình
            e.printStackTrace();
            // Có thể quyết định xử lý khác tùy thuộc vào yêu cầu
        }
    }
}