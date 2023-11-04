package com.fanstatic.extensions;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OtpGenerator {

    private static final String TOTP_ALGORITHM = "HmacSHA1";
    private static final int TOTP_DIGITS = 6;
    private static final int TOTP_PERIOD = 30;

    public static String generateOtp(String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            long currentTime = Instant.now().getEpochSecond();
            long timeStep = currentTime / TOTP_PERIOD;

            byte[] timeBytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(timeStep).array();

            Mac mac = Mac.getInstance(TOTP_ALGORITHM);
            mac.init(new SecretKeySpec(keyBytes, TOTP_ALGORITHM));
            byte[] hashBytes = mac.doFinal(timeBytes);

            int offset = hashBytes[hashBytes.length - 1] & 0x0f;

            int code = ((hashBytes[offset] & 0x7f) << 24) |
                    ((hashBytes[offset + 1] & 0xff) << 16) |
                    ((hashBytes[offset + 2] & 0xff) << 8) |
                    (hashBytes[offset + 3] & 0xff);

            code %= Math.pow(10, TOTP_DIGITS);

            return String.format("%0" + TOTP_DIGITS + "d", code);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("OTP generation failed", e);
        }
    }
}
