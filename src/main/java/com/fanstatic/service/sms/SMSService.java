package com.fanstatic.service.sms;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;

@Service
public class SMSService {
    private static final String BASE_URL = "https://rg36ll.api.infobip.com";
    private static final String API_KEY = "45899dbfc865030a2147393959757b7a-6cf480f4-4b76-4bb1-95e2-6fa092802363";

    public boolean sendSMS(String to, String message) {
        var apiClient = ApiClient.forApiKey(ApiKey.from(API_KEY))
                .withBaseUrl(BaseUrl.from(BASE_URL))
                .build();
        var sendSmsApi = new SmsApi(apiClient);

        // Create a message to send.
        var smsMessage = new SmsTextualMessage()
                .addDestinationsItem(new SmsDestination().to(to))
                .text(message);

        // Create a send message request.
        var smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            // Send the message.
            var smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest).execute();
            // System.out.println("Response body: " + smsResponse);

            // Get delivery reports. It may take a few seconds to show the above-sent
            // message.
            var reportsResponse = sendSmsApi.getOutboundSmsMessageDeliveryReports().execute();
            // System.out.println(reportsResponse.getResults());
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

}
