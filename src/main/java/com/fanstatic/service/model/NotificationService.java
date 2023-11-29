package com.fanstatic.service.model;

import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.repository.NotificationRepository;
import com.fanstatic.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private final String DETAIL_TO_ORDER = "";

    public boolean sendOrderCreate(Integer orderId) {
        String message = "Order mới được tạo. Mã order: " + orderId;
        String action = ApplicationConst.CLIENT_HOST + "/" + DETAIL_TO_ORDER + "/" + orderId;

        return true;
    }
}
