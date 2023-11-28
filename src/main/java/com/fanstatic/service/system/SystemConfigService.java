package com.fanstatic.service.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.system.ContactConfig;
import com.fanstatic.config.system.IpConfig;
import com.fanstatic.config.system.PointProgramConfig;
import com.fanstatic.config.system.SystemConfig;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.util.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemConfigService {
    private final ObjectMapper objectMapper;

    private SystemConfig systemConfig;

    public ResponseDTO getConfig() {
        loadConfig();
        return ResponseUtils.success(200, "Cấu hình hệ thống", systemConfig);
    }

    public ResponseDTO getContactConfig() {
        loadConfig();

        return ResponseUtils.success(200, "Cấu hình hệ thống", systemConfig.getContact());
    }

    public List<IpConfig> getListIpConfig() {
        loadConfig();
        return systemConfig.getIpConfigs();
    }

    public ResponseDTO getIpConfig() {
        loadConfig();
        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        List<IpConfig> ipConfigs = systemConfig.getIpConfigs();
        List<ResponseDataDTO> iResponseDataDTOs = new ArrayList<>();
        for (IpConfig ipConfig : ipConfigs) {
            iResponseDataDTOs.add(ipConfig);
        }
        responseListDataDTO.setDatas(iResponseDataDTOs);
        return ResponseUtils.success(200, "Cấu hình hệ thống", responseListDataDTO);
    }

    public PointProgramConfig getPointProgramConfigModel() {
        loadConfig();
        return systemConfig.getPointProgram();
    }

    public ResponseDTO getPointProgramConfig() {
        loadConfig();
        return ResponseUtils.success(200, "Cấu hình hệ thống",
                systemConfig.getPointProgram());
    }

    public ResponseDTO updateConfig(SystemConfig newConfig) {
        this.systemConfig = newConfig;
        saveConfig();
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);
    }

    public ResponseDTO updateContactConfig(ContactConfig newContactConfig) {
        loadConfig();

        systemConfig.setContact(newContactConfig);
        saveConfig();
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO updateIpConfig(List<IpConfig> ipconfigs) {
        loadConfig();
        systemConfig.getIpConfigs().clear();
        systemConfig.setIpConfigs(ipconfigs);
        saveConfig();
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO updatePointProgram(PointProgramConfig pointProgramConfig) {
        loadConfig();
        systemConfig.setPointProgram(pointProgramConfig);
        saveConfig();
        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    private ResponseDTO loadConfig() {
        try {

            File configFile = new File("src\\main\\resources\\config\\config.json");
            systemConfig = objectMapper.readValue(configFile, SystemConfig.class);
            return ResponseUtils.success(200, "Cấu hình hệ thống", systemConfig);
        } catch (IOException e) {
            // Xử lý lỗi khi không thể đọc cấu hình
            e.printStackTrace();
            return ResponseUtils.fail(500, "Không thể đọc dữ liệu", null);
        }
    }

    private ResponseDTO saveConfig() {
        try {
            File configFile = new File("src\\main\\resources\\config\\config.json");
            objectMapper.writeValue(configFile, systemConfig);
            return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

        } catch (IOException e) {
            // Xử lý lỗi khi không thể ghi cấu hình
            e.printStackTrace();
            return ResponseUtils.fail(500, "Không thể đọc dữ liệu", null);

        }
    }
}