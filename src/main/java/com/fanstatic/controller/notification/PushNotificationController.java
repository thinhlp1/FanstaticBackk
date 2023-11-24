package com.fanstatic.controller.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.system.SubscribteNotiRequestDTO;
import com.fanstatic.service.fcm.FCMService;
import com.fanstatic.service.system.PushNotificationService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    // @PostMapping("/fcm/send")
    // public String sendMessage() {
    // return fcmService.sendMessage(
    // "fY8_LjrNggysIbpwU-_5I6:APA91bFJSHLHlEbqL9wr0wb9ShcfgDMGkkAF2SJdgONoZc2n2xi0pMeRPf2sPtiVVVyB0iMin9fjYCDwdhfeFmqcSu_hc4NmKJJmZMqAXPeXW4_Qd-QmdHWulv41E46-ZzlIGv4-RYFa",
    // "high", "Thông báo mới Fanstatic", "Ưu đãi hấp dẫn 20/11 tại Fanstatic",
    // "https://fantastic-ui-chi.vercel.app/icon/favicon.svg",
    // "https://fantastic-5vesy0pcb-ltkien2003.vercel.app/");
    // }

    @PostMapping("/notification/subscribe")
    public ResponseEntity<ResponseDTO> subscribeNotification(
            @RequestBody @Valid SubscribteNotiRequestDTO subscribteNotiRequestDTO) {
        ResponseDTO reponseDTO = pushNotificationService.subscribe(subscribteNotiRequestDTO);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    
}
