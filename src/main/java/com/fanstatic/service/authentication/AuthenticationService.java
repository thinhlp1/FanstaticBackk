package com.fanstatic.service.authentication;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.AuthenReponse;
import com.fanstatic.dto.auth.ConfirmOtpDTO;
import com.fanstatic.dto.auth.ConfirmPasswordDTO;
import com.fanstatic.dto.auth.LoginAccountDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.auth.LoginPasswordDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.User;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.repository.RoleRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.service.model.AccountService;
import com.fanstatic.service.system.OTPService;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.CookieUtils;
import com.fanstatic.util.JwtUtil;
import com.fanstatic.util.ResponseUtils;
import com.fanstatic.util.SessionUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final SystemService systemService;
    private final CookieUtils cookieUtils;
    private final UserRepository userRepository;
    private final SessionUtils sessionUtils;
    private final OTPService otpService;
    private final AccountService accountService;

    public ResponseDTO login(LoginAccountDTO loginDTO) {
        AuthenReponse authenReponse = new AuthenReponse();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));
        } catch (Exception e) {
            return ResponseUtils.fail(401, "Sai tên tài khoản hoặc mật khẩu", null);
        }

        try {

            Account account = accountRepository.findByNumberPhoneAndActiveIsTrue(loginDTO.getNumberPhone())
                    .orElseThrow();
            String jwtToken = jwtUtil.generateToken(account);
            authenReponse.setToken(jwtToken);
            cookieUtils.set("token", jwtToken, 24);
            systemService.writeLoginLog(jwtToken, account.getUser());
            // return authenReponse;
            return ResponseUtils.success(200, "Đăng nhập thành công", authenReponse);

        } catch (Exception e) {
            return ResponseUtils.fail(500, "Có lỗi xảy ra khi đăng nhập", null);

        }
    }

    public ResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(loginDTO.getNumberPhone()).orElse(null);
        if (user == null) {
            sessionUtils.set("numberPhone", loginDTO.getNumberPhone());
            boolean isSended = otpService.sendOTP();
            if (isSended) {
                return ResponseUtils.success(200, "Gửi mã OTP thành công", null);
            } else {
                return ResponseUtils.fail(500, "Lỗi gửi mã OTP", null);
            }

        }
        sessionUtils.set("numberPhone", loginDTO.getNumberPhone());
        sessionUtils.set("accountExits", true);

        return ResponseUtils.success(201, "Tài khoản có tồn tại", null);

    }

    public ResponseDTO confirmOTP(@Valid ConfirmOtpDTO confirmOtpDTO) {

        if (sessionUtils.get("numberPhone") == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);

        }

        boolean isValid = otpService.validateOTP(confirmOtpDTO.getOtp());
        if (isValid) {
            sessionUtils.set("isConfirm", true);
            return ResponseUtils.success(200, "OPT Hợp lệ", null);
        }
        return ResponseUtils.fail(500, "OTP không hợp lệ", null);

    }

    public ResponseDTO resendOTP() {
        if (sessionUtils.get("numberPhone") == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);
        }

        boolean isSended = otpService.sendOTP();
        if (isSended) {
            return ResponseUtils.success(200, "Gửi mã OTP thành công", null);
        } else {
            return ResponseUtils.fail(500, "Lỗi gửi mã OTP", null);
        }
    }

    public ResponseDTO confirmPassword(ConfirmPasswordDTO confirmPasswordDTO) {
        String numberPhone = sessionUtils.get("numberPhone");
        if (sessionUtils.get("isConfirm") == null || numberPhone == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);
        }

        User user = userRepository.findByNumberPhoneAndActiveIsTrue(numberPhone).orElse(null);
        if (user == null) {
            user = new User();
            user.setNumberPhone(numberPhone);
            user.setCreateAt(new Date());
            user.setRole(roleRepository.findById(ApplicationConst.CUSTOMER_ROLE_ID).get());
            user.setActive(DataConst.ACTIVE_TRUE);
            User userSaved = userRepository.saveAndFlush(user);
            if (userSaved != null) {
                Account account = new Account();
                account.setPassword(passwordEncoder.encode(confirmPasswordDTO.getPassword()));
                account.setNumberPhone(numberPhone);

                account.setUser(user);
                account.setRole(user.getRole());
                account.setActive(DataConst.ACTIVE_TRUE);
                account.setCreateAt(new Date());
                // TODO thay user 1 thanh user dang dang nhap vao he thong

                account = accountRepository.save(account);
                Account accountSave = accountRepository.save(account);
                if (accountSave != null) {
                    sessionUtils.set("accountExits", true);
                    return loginPassword(new LoginPasswordDTO(confirmPasswordDTO.getPassword()));

                } else {
                    return ResponseUtils.fail(500, "Tạo thất bại", null);

                }
            }

        } else {
            return ResponseUtils.fail(500, "Reset lại pass", null);

        }
        return ResponseUtils.fail(500, "Xác nhận không thành công", null);

    }

    public ResponseDTO loginPassword(LoginPasswordDTO loginDTO) {
        String numberPhone = sessionUtils.get("numberPhone");
        if (numberPhone == null || sessionUtils.get("accountExits") == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);
        }

        LoginAccountDTO loginAccountDTO = new LoginAccountDTO(numberPhone, loginDTO.getPassword());
        return login(loginAccountDTO);
    }

    public ResponseDTO forgetConfirmPassword(@Valid ConfirmPasswordDTO confirmPasswordDTO) {
        String numberPhone = sessionUtils.get("numberPhone");
        if (sessionUtils.get("isConfirm") == null || numberPhone == null) {
            return ResponseUtils.fail(500, "Chưa đăng nhập", null);
        }
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(numberPhone).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(500, "Số điện thoại chưa đăng ký", null);
        }
        Account account = accountRepository.findByNumberPhoneAndActiveIsTrue(numberPhone).orElse(null);

        if (account == null) {
            return ResponseUtils.fail(500, "Số điện thoại chưa đăng ký", null);
        }

        account.setPassword(passwordEncoder.encode(confirmPasswordDTO.getPassword()));
        Account accountSaved = accountRepository.save(account);
        if (accountSaved != null) {
            sessionUtils.set("accountExits", true);
            return loginPassword(new LoginPasswordDTO(confirmPasswordDTO.getPassword()));
        } else {
            return ResponseUtils.fail(500, "Reset không thành công", null);

        }
    }

}