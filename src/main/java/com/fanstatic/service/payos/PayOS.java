package com.fanstatic.service.payos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public String generateSignature(JSONObject data) {
        JSONObject jsonObject = data;
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

    public JSONObject transaction(int orderCode, Long amount, String description) {
        JSONObject data = new JSONObject();
        data.put("orderCode", orderCode);
        data.put("amount", amount);
        data.put("description", description);
        data.put("cancelUrl", cancelUrl);
        data.put("returnUrl", returnUrl);
        data.put("signature", generateSignature(data));
        return data;
    }
}
