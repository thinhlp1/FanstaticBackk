package com.fanstatic.service.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SMSService {
    @Value("${twilio.sms.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.sms.auth.token}")
    private String AUTH_TOKEN;

    public boolean sendSMS(String to, String message) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(new PhoneNumber(to),
            new PhoneNumber("+13345084368"), message).create();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
