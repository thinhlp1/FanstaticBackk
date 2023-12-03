package com.fanstatic.service.system;

import com.fanstatic.dto.system.OtpDTO;
import com.fanstatic.extensions.OtpGenerator;
import com.fanstatic.service.sms.SMSService;
import com.fanstatic.util.SessionUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class OTPService {
    private final SessionUtils sessionUtils;
    @Autowired
    SMSService smsService;

    public boolean sendOTP() {
        String otp = OtpGenerator.generateOtp("otp");
        String phone = "+84" + sessionUtils.get("numberPhone").toString().substring(1);
        // smsService.sendSMS( phone, "Mã xác nhận của bạn là: " + otp);
        System.out.println("PHONE: " + phone);
        System.out.println("OTP: " + otp);
        setOTP(otp);
        return true;

    }

    public boolean setOTP(String otp) {

        String numberPhone = sessionUtils.get("numberPhone");
        if (numberPhone == null) {
            return false;
        }

        OtpDTO otpDTO = new OtpDTO();

        long expirationTimeMillis = new Date().getTime() + 180 * 1000;

        otpDTO.setOtp(otp);
        otpDTO.setPhoneNumber(numberPhone);
        otpDTO.setExpirationTime(expirationTimeMillis);

        System.err.println("OTP xong: " + otp);

        sessionUtils.set(numberPhone + "_OTP", otpDTO);
        return true;
    }

    public boolean validateOTP(String otpInput) {

        String numberPhone = sessionUtils.get("numberPhone");
        if (numberPhone == null) {
            return false;
        }

        OtpDTO otpDTO = sessionUtils.get(numberPhone + "_OTP");
        if (otpDTO == null) {
            return false;
        }

        long expirationTime = otpDTO.getExpirationTime();
        long currentTime = System.currentTimeMillis();

        // Kiểm tra xem thời gian hiện tại có lớn hơn thời gian hết hạn (expirationTime)
        // hay không
        if (currentTime > expirationTime) {
            // OTP đã hết hạn
            return false;
        }

        if (otpInput != null && otpInput.equals(otpDTO.getOtp())) {

            sessionUtils.remove(numberPhone + "_OTP");

            return true;
        }

        return false;
    }

    public String generateOtpCode() {
        int otpLength = 6; // độ dài mã OTP
        String numbers = "0123456789"; // chuỗi chứa các ký tự sẽ được sử dụng để tạo mã OTP
        Random random = new Random();
        char[] otp = new char[otpLength];
        for (int i = 0; i < otpLength; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    // Hàm kiểm tra tính hợp lệ của mã OTP
    public boolean isValidOtpCode(String otp) {
        return otp.matches("[0-9]{6}");
    }
}
