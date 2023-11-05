package com.fanstatic.service.payos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;

@Service
public class PayOS {

    @Value("${payos.checkout.url}")
    private String checkoutUrl;

    @Value("${payos.checksum.key}")
    private String checksumKey;

    @Value("${payos.return.url}")
    private String returnUrl;

    @Value("${payos.cancel.url}")
    private String cancelUrl;

    public static Iterator<String> sortedIterator(Iterator<String> it, Comparator<String> comparator) {
        List<String> list = new ArrayList<String>();
        while (it.hasNext()) {
            list.add(it.next());
        }

        Collections.sort(list, comparator);
        return list.iterator();
    }

    public String generateSignature(String data) {
        JSONObject jsonObject = new JSONObject(data);
        Iterator<String> sortedIt = sortedIterator(jsonObject.keys(), (a, b) -> a.compareTo(b));

        StringBuilder transactionStr = new StringBuilder();
        while (sortedIt.hasNext()) {
            String key = sortedIt.next();
            String value = jsonObject.get(key).toString();
            transactionStr.append(key);
            transactionStr.append('=');
            transactionStr.append(value);
            if (sortedIt.hasNext()) {
                transactionStr.append('&');
            }
        }

        String signature = new HmacUtils("HmacSHA256", checksumKey).hmacHex(transactionStr.toString());
        return signature;
    }

    public String transaction(int orderCode, Long amount, String description, String transactionDatetime) {
        String data = "{" + "\"orderCode\":" + orderCode + "," + "\"amount\":" + amount + "," + "\"description\":"
                + description + "," + "\"transactionDatetime\":" + transactionDatetime + "}" + "," + "\"cancelUrl\":"
                + cancelUrl + "," + "\"returnUrl\":" + returnUrl + "," + "\"checkoutUrl\":" + checkoutUrl + "}";
        return data;
    }
}
