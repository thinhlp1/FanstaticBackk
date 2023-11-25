package com.fanstatic.service.model;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.requestStaff.CreateRequestStaffNotificationDTO;
import com.fanstatic.dto.model.requestStaff.RequestStaffNotificationDTO;
import com.fanstatic.model.RequestStaffNotification;
import com.fanstatic.model.User;
import com.fanstatic.repository.RequestStaffNotificationRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestStaffNotifacationService {
    private final RequestStaffNotificationRepository requestStaffNotificationRepository;
    private final SystemService systemService;
    private final ModelMapper modelMapper;

    public ResponseDTO create(CreateRequestStaffNotificationDTO createRequestStaffNotificationDTO) {
        RequestStaffNotification requestStaffNotification = new RequestStaffNotification();
        User customer = systemService.getUserLogin();

        requestStaffNotification.setCustomer(customer);
        requestStaffNotification.setContent(createRequestStaffNotificationDTO.getContent());
        requestStaffNotification.setCreateAt(new Date());

        RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                .saveAndFlush(requestStaffNotification);

        RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotificationSaved,
                RequestStaffNotificationDTO.class);

        return ResponseUtils.success(200, "Yêu cầu thành công", requestStaffNotificationDTO);
    }

    public ResponseDTO confirm(Integer id) {
        RequestStaffNotification requestStaffNotification = requestStaffNotificationRepository.findById(id)
                .orElse(null);
        if (requestStaffNotification == null) {
            return ResponseUtils.fail(200, "Yêu cầu không tồn tại", null);
        }
        User employeeConfirm = systemService.getUserLogin();

        requestStaffNotification.setEmployeeConfirm(employeeConfirm);
        requestStaffNotification.setConfirmAt(new Date());

        RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                .saveAndFlush(requestStaffNotification);

        RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotificationSaved,
                RequestStaffNotificationDTO.class);

        return ResponseUtils.success(200, "Xác nhận thành công", requestStaffNotificationDTO);
    }

    // public ResponseDTO show(int active) {
    //     List<>
    //     switch (active) {
    //         case RequestParamConst.ACTIVE_ALL:
    //             products = productRepository.findAllByOrderByCreateAtDesc();
    //             break;
    //         case RequestParamConst.ACTIVE_TRUE:
    //             products = productRepository.findAllByActiveIsTrueOrderByCreateAtDesc().orElse(products);
    //             break;
    //         case RequestParamConst.ACTIVE_FALSE:
    //             products = productRepository.findAllByActiveIsFalseOrderByCreateAtDesc().orElse(products);
    //             break;
    //         default:
    //             products = productRepository.findAllByOrderByCreateAtDesc();
    //             break;
    //     }
    // }
}
