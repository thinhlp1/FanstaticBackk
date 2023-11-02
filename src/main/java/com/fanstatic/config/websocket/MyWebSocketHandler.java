package com.fanstatic.config.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Xử lý khi kết nối WebSocket được thiết lập
        System.out.println("WebSocket connection established");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Xử lý tin nhắn văn bản nhận được từ WebSocket
        System.out.println("Received message: " + message.getPayload());

        // Gửi tin nhắn trả lời đến WebSocket client
        String replyMessage = "Hello from server!";
        session.sendMessage(new TextMessage(replyMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Xử lý khi kết nối WebSocket bị đóng
        System.out.println("WebSocket connection closed");
    }
}