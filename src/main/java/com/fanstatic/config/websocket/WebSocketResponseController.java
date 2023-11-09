package com.fanstatic.config.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fanstatic.dto.ResponseDTO;

@Controller
public class WebSocketResponseController {

    private final SimpMessagingTemplate messagingTemplate;


    public WebSocketResponseController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendWebSocketResponse(ResponseDTO responseDTO, String topic) {
        // Gửi thông điệp tới các khách hàng WebSocket
        messagingTemplate.convertAndSend(topic, responseDTO);
    }
}
