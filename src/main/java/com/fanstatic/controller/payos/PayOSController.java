package com.fanstatic.controller.payos;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

public class PayOSController {
    @RestController
    public class RedirectController {
        @PostMapping("/checkout")
        public ResponseEntity<String> redirectWithDefaultHeadersAndJson() {
            UriComponents redirectUri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/checkout")
                    .build();
            String defaultXApiKey = "308a2d55-b19f-4f8d-97eb-3916b3d149ea";
            String defaultXClientId = "758fc62c-bcfe-4294-9661-5b7d2c1be420";
            String defaultJsonBody = "{\"key\": \"value\"}";
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", defaultXApiKey);
            headers.add("x-client-id", defaultXClientId);
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.status(302)
                    .location(redirectUri.toUri())
                    .headers(headers)
                    .body(defaultJsonBody);
        }
    }
}
