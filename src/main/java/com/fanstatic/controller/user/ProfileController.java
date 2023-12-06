package com.fanstatic.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.ChangePasswordDTO;
import com.fanstatic.dto.auth.ConfirmOtpDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.service.model.NotificationService;
import com.fanstatic.service.user.UserProfileService;
import com.fanstatic.util.ResponseUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;
    private final NotificationService notificationService;

    @GetMapping("/user-profile")
    @ResponseBody
    public ResponseEntity<ResponseDTO> showUserProfile() {
        ResponseDTO responseDTO = userProfileService.getUserProfile();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/update/image")
    @ResponseBody
    public ResponseEntity<ResponseDTO> updateImage(@RequestPart MultipartFile image) {
        ResponseDTO reponseDTO = userProfileService.updateImage( image);
        return ResponseUtils.returnReponsetoClient(reponseDTO);
    }

    @GetMapping("/notification")
    @ResponseBody
    public ResponseEntity<ResponseDTO> getNotification() {
        ResponseDTO responseDTO = notificationService.getNotification();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/seen/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> seen(@PathVariable("id") Integer id) {
        ResponseDTO responseDTO = notificationService.seenNotification(id);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PutMapping("/collect-voucher/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDTO> collectVoucher(@PathVariable("id") Integer voucherId) {
        ResponseDTO responseDTO = userProfileService.collectVoucher(voucherId);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/change-numberphone")
    @ResponseBody
    public ResponseEntity<ResponseDTO> changeNumberPhone(@RequestBody @Valid LoginDTO loginDTO) {
        ResponseDTO responseDTO = userProfileService.changeNumberPhone(loginDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<ResponseDTO> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        ResponseDTO responseDTO = userProfileService.changePassword(changePasswordDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @PostMapping("/confirm-otp")
    @ResponseBody
    public ResponseEntity<ResponseDTO> confirmOTP(@RequestBody @Valid ConfirmOtpDTO confirmOtpDTO) {
        ResponseDTO responseDTO = userProfileService.confirmOTP(confirmOtpDTO);
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }

    @GetMapping("/resend-otp")
    @ResponseBody
    public ResponseEntity<ResponseDTO> resendOTP() {
        ResponseDTO responseDTO = userProfileService.resendOTP();
        return ResponseUtils.returnReponsetoClient(responseDTO);
    }
}
