package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.requestStaff.CreateRequestStaffNotificationDTO;
import com.fanstatic.dto.model.requestStaff.RequestStaffNotificationDTO;
import com.fanstatic.dto.model.user.UserCompactDTO;
import com.fanstatic.model.RequestStaffNotification;
import com.fanstatic.model.User;
import com.fanstatic.repository.RequestStaffNotificationRepository;
import com.fanstatic.service.system.PushNotificationService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestStaffNotifacationService {
    private final RequestStaffNotificationRepository requestStaffNotificationRepository;
    private final SystemService systemService;
    private final PushNotificationService pushNotificationService;

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
        pushNotificationToUser(requestStaffNotification.getCustomer().getId(), "Nhân viên đang tới",
                "Order của bạn đã được tiếp nhận");
        return ResponseUtils.success(200, "Xác nhận thành công", requestStaffNotificationDTO);
    }

    public ResponseDTO deny(Integer id) {
        RequestStaffNotification requestStaffNotification = requestStaffNotificationRepository.findById(id)
                .orElse(null);
        if (requestStaffNotification == null) {
            return ResponseUtils.fail(404, "Yêu cầu không tồn tại", null);
        }

        if (requestStaffNotification.getEmployeeConfirm() != null) {
            return ResponseUtils.fail(400, "Yêu cầu đã bị nhân viên từ chối",
                    modelMapper.map(requestStaffNotification.getEmployeeConfirm(), UserCompactDTO.class));

        }
        User employeeConfirm = systemService.getUserLogin();

        requestStaffNotification.setEmployeeConfirm(employeeConfirm);
        requestStaffNotification.setDenyAt(new Date());

        RequestStaffNotification requestStaffNotificationSaved = requestStaffNotificationRepository
                .saveAndFlush(requestStaffNotification);

        RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotificationSaved,
                RequestStaffNotificationDTO.class);
        pushNotificationToUser(requestStaffNotification.getCustomer().getId(), "Nhân viên đang tới",
                "Order của bạn đã được tiếp nhận");

        return ResponseUtils.success(200, "Xác nhận thành công", requestStaffNotificationDTO);
    }

    public ResponseDTO show() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Tính thời điểm 24 giờ

        List<RequestStaffNotification> requestStaffNotifications = requestStaffNotificationRepository
                .findAllInTime(twentyFourHoursAgo);
        List<ResponseDataDTO> requestStaffNotificationDTOs = new ArrayList<>();

        for (RequestStaffNotification requestStaffNotification : requestStaffNotifications) {
            RequestStaffNotificationDTO requestStaffNotificationDTO = modelMapper.map(requestStaffNotification,
                    RequestStaffNotificationDTO.class);
            requestStaffNotificationDTOs.add(requestStaffNotificationDTO);
        }

        ResponseListDataDTO responseListDataDTO = new ResponseListDataDTO();
        responseListDataDTO.setDatas(requestStaffNotificationDTOs);
        responseListDataDTO.setNameList("Danhh sách gọi nhân viên");

        return ResponseUtils.success(200, "Danh sach gọi nhân viên", responseListDataDTO);
    }

    private void pushNotificationToUser(Integer userId, String title, String body) {
        String url = ApplicationConst.CLIENT_HOST;
        pushNotificationService.pushNotification(userId, PushNotificationService.HIGT, title, body,
                url);
    }
}
