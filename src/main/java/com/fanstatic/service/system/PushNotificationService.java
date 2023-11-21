package com.fanstatic.service.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.system.SubscribteNotiRequestDTO;
import com.fanstatic.model.BrowserToken;
import com.fanstatic.model.User;
import com.fanstatic.repository.BrowserTokenRepository;
import com.fanstatic.service.fcm.FCMService;
import com.fanstatic.util.ResponseUtils;
import com.twilio.twiml.messaging.Body;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final BrowserTokenRepository browserTokenRepository;
    private final SystemService systemService;
    @Autowired
    private final FCMService fcmService;

    public static final String HIGT = "high";

    public ResponseDTO subscribe(SubscribteNotiRequestDTO subscribteNotiRequestDTO) {
        User user = systemService.getUserLogin();

        BrowserToken existingToken = browserTokenRepository.findByUserAndToken(user,
                subscribteNotiRequestDTO.getToken()).orElse(null);

        if (existingToken == null) {
            BrowserToken newToken = new BrowserToken();
            newToken.setUser(user);
            newToken.setToken(subscribteNotiRequestDTO.getToken());
            newToken.setBrowser(subscribteNotiRequestDTO.getBrowser());
            newToken.setSubscribeAt(new Date());
            browserTokenRepository.save(newToken);
        }
        return ResponseUtils.success(200, "Nhận thông báo thành công", null);
    }

    public ResponseDTO pushNotification(User user, String priority, String title, String body, String returnUrl) {

        List<BrowserToken> browserTokens = browserTokenRepository.findByUser(user);

        if (browserTokens != null) {
            for (BrowserToken browserToken : browserTokens) {
                String isSend = fcmService.sendMessage(browserToken.getToken(), priority, title, body,
                        ApplicationConst.CLIENT_HOST_ICON, returnUrl);
                        System.out.println(title);
                        System.out.println(browserToken.getToken());
                if (isSend == null) {
                    System.out.println("KO THÀNH CÔNG");
                }else{
                    System.out.println("THÀNH CÔNG");
                }

            }
        }

        return ResponseUtils.success(200, "Nhận thông báo thành công", null);

    }

}
