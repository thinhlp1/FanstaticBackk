package com.fanstatic.controller.order;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.dto.ResponseDTO;

@Controller
public class WSPurcharseOrderController {

    private final SimpMessagingTemplate messagingTemplate;

    public WSPurcharseOrderController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendWebSocketResponse(Integer orderId, ResponseDTO responseDTO) {
        System.err.println("/topic/order/detail/" + orderId);
        messagingTemplate.convertAndSend("/topic/order/detail/" + orderId,
                responseDTO);

    }

    public void sendWebSocketResponse(ResponseDTO responseDTO, String topic) {
        // Gửi thông điệp tới các khách hàng WebSocket
        messagingTemplate.convertAndSend(topic, responseDTO);
    }



}
