package com.fanstatic.service.system;

import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.WebsocketConst;
import com.fanstatic.config.websocket.WebSocketResponseController;
import com.fanstatic.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebsocketService {
    private final WebSocketResponseController webSocketResponseController;

    public void sendNewOrder(ResponseDTO responseDTO) {

        webSocketResponseController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_PURCHARSE_ORDER);
        sendDetailsOrder(responseDTO);

    }

    public void sendDetailsOrder(ResponseDTO responseDTO) {
        webSocketResponseController.sendWebSocketResponse(responseDTO, WebsocketConst.TOPIC_ORDER_DETAILS);

    }
}
