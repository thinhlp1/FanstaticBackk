package com.fanstatic.dto.system;

import com.google.api.client.util.DateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpDTO {

    private String otp;

    private String phoneNumber;

    private long expirationTime;
}
