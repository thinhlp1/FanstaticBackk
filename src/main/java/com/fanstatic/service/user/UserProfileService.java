package com.fanstatic.service.user;

import java.util.ArrayList;
import java.util.List;

import javax.management.modelmbean.ModelMBean;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.auth.ChangePasswordDTO;
import com.fanstatic.dto.auth.ConfirmOtpDTO;
import com.fanstatic.dto.auth.ConfirmPasswordDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.auth.LoginPasswordDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.profile.ProfileUserDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.User;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.model.CustomerService;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.service.system.OTPService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.CookieUtils;
import com.fanstatic.util.JwtUtil;
import com.fanstatic.util.ResponseUtils;
import com.fanstatic.util.SessionUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final SystemService systemService;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SessionUtils sessionUtils;
    private final OTPService otpService;
    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager transactionManager;

    public ResponseDTO getUserProfile() {
        try {
            User customer = systemService.getUserLogin();
            CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
            if (customer.getImage() != null) {
                String imageUrl = customer.getImage().getLink();
                customerDTO.setImageUrl(imageUrl);
            }

            ResponseDTO responseDTO = orderService.getListOrder(customer.getId());
            List<ResponseDataDTO> orderDTOs = new ArrayList<>();
            if (responseDTO.isSuccess()) {
                ResponseListDataDTO responseListDataDTO = (ResponseListDataDTO) responseDTO.getData();
                orderDTOs = responseListDataDTO.getDatas();
            }

            ProfileUserDTO profileUserDTO = new ProfileUserDTO();
            profileUserDTO.setCustomerDTO(customerDTO);
            profileUserDTO.setOrderDTOs(orderDTOs);

            return ResponseUtils.success(200, "Profile user", profileUserDTO);

        } catch (Exception e) {
            return ResponseUtils.fail(500, "Có lỗi xảy ra", null);

        }
    }

    public ResponseDTO changeNumberPhone(LoginDTO loginDTO) {
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(loginDTO.getNumberPhone()).orElse(null);
        if (user == null) {
            System.out.println(loginDTO.getNumberPhone());
            sessionUtils.set("numberPhone", loginDTO.getNumberPhone());
            boolean isSended = otpService.sendOTP();
            if (isSended) {
                return ResponseUtils.success(200, "Gửi mã OTP thành công", null);
            } else {
                return ResponseUtils.fail(500, "Lỗi gửi mã OTP", null);
            }

        }
        return ResponseUtils.success(201, "Số điện thoại đã được sử dụng", null);

    }

    public ResponseDTO confirmOTP(ConfirmOtpDTO confirmOtpDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        if (sessionUtils.get("numberPhone") == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);

        }

        boolean isValid = otpService.validateOTP(confirmOtpDTO.getOtp());
        if (isValid) {

            try {
                User user = systemService.getUserLogin();
                user.setNumberPhone(sessionUtils.get("numberPhone").toString());
                userRepository.save(user);

                Account account = user.getAccount();
                account.setNumberPhone(user.getNumberPhone());
                accountRepository.save(account);

                // change token after change numberphone
                String jwtToken = jwtUtil.generateToken(account);
                cookieUtils.set("token", jwtToken, 24);
                // systemService.writeLoginLog(jwtToken, account.getUser());
                // systemService.writeSystemLog(user.getId(), user.getName(), "Thay đổi số điện
                // thoại");

                sessionUtils.remove("numberPhone");

                transactionManager.commit(transactionStatus);
            } catch (Exception e) {
                e.printStackTrace();
                transactionManager.rollback(transactionStatus);
            }

            return ResponseUtils.success(200, "Đổi số điện thoại thành công", null);
        }
        return ResponseUtils.fail(500, "OTP không hợp lệ", null);

    }

    public ResponseDTO resendOTP() {
        if (sessionUtils.get("numberPhone") == null) {
            return ResponseUtils.fail(500, "Chưa nhập số điện thoại mới", null);
        }

        boolean isSended = otpService.sendOTP();
        if (isSended) {
            return ResponseUtils.success(200, "Gửi mã OTP thành công", null);
        } else {
            return ResponseUtils.fail(500, "Lỗi gửi mã OTP", null);
        }
    }

    public ResponseDTO changePassword(ChangePasswordDTO changePasswordDTO) {

        User user = systemService.getUserLogin();
        Account account = user.getAccount();



        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), account.getPassword())) {
            return ResponseUtils.fail(400, "Mật khẩu không đúng", null);
        }

        if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getPassword())) {
            return ResponseUtils.fail(400, "Mật khẩu không khóp", null);

        }

        account.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        Account accountSaved = accountRepository.save(account);
        if (accountSaved != null) {
            sessionUtils.set("accountExits", true);
            String jwtToken = jwtUtil.generateToken(account);
            cookieUtils.set("token", jwtToken, 24);
            return ResponseUtils.success(200, "Đổi mật khẩu thành công", null);

        } else {
            return ResponseUtils.fail(500, "Đổi mật khẩu không thành công", null);

        }
    }

    // public ResponseDTO changeNumberPhone(LoginDTO loginDTO) {
    // User customer = systemService.getUserLogin();

    // ResponseDTO responseDTO =

    // return ResponseUtils.success(200, "Profile user", null);

    // }

}
