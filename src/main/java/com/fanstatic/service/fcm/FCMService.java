package com.fanstatic.service.fcm;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

@Service
public class FCMService {

    @Value("${fcm.authorization.key}")
    private String authorizationKey;

    @Value("${fcm.url}")
    private String fcmUrl;

    public JsonObject notification(String to, String priority, String title, String body, String icon, String clickAction) {
        JsonObject notification = new JsonObject();
        notification.addProperty("to", to);
        notification.addProperty("priority", priority);
        notification.addProperty("content_available", false);
        JsonObject notificationData = new JsonObject();
        notificationData.addProperty("title", title);
        notificationData.addProperty("body", body);
        notificationData.addProperty("icon", icon);
        notificationData.addProperty("click_action", clickAction);
        notification.add("notification", notificationData);
        return notification;
    }
    public String sendMessage(String to, String priority, String title, String body, String icon, String clickAction) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "key=" + authorizationKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonObject data = notification(to, priority, title, body, icon, clickAction);
        HttpEntity<String> entity = new HttpEntity<>(data.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(fcmUrl, HttpMethod.POST, entity, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        int codeId = jsonObject.getInt("success");
        if (codeId == 1) {
            return "Gửi tin nhắn thành công";
        }
        return null;
    }
    
}
