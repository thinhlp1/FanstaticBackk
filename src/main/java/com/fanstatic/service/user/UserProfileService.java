package com.fanstatic.service.user;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.modelmbean.ModelMBean;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.config.constants.ImageConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.auth.AuthenReponse;
import com.fanstatic.dto.auth.ChangePasswordDTO;
import com.fanstatic.dto.auth.ConfirmOtpDTO;
import com.fanstatic.dto.auth.ConfirmPasswordDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.dto.auth.LoginPasswordDTO;
import com.fanstatic.dto.model.customer.CustomerDTO;
import com.fanstatic.dto.model.order.OrderDTO;
import com.fanstatic.dto.model.profile.ProfileUpdateDTO;
import com.fanstatic.dto.model.profile.ProfileUserDTO;
import com.fanstatic.dto.model.shift.ShiftRequestDTO;
import com.fanstatic.dto.model.user.UserDTO;
import com.fanstatic.dto.model.voucher.VoucherDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.File;
import com.fanstatic.model.Shift;
import com.fanstatic.model.User;
import com.fanstatic.model.UserVoucher;
import com.fanstatic.model.Voucher;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.repository.UserRepository;
import com.fanstatic.repository.UserVoucherRepository;
import com.fanstatic.repository.VoucherRepository;
import com.fanstatic.service.model.CustomerService;
import com.fanstatic.service.model.RolePermissionService;
import com.fanstatic.service.order.OrderService;
import com.fanstatic.service.system.FileService;
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
    private final OrderService orderService;
    private final SystemService systemService;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final FileService fileService;
    private final RolePermissionService rolePermissionService;

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

            List<Voucher> vouchers = userVoucherRepository.findActiveVouchersForUser(customer.getId(), new Date());
            List<VoucherDTO> voucherDTOs = new ArrayList<>();
            for (Voucher voucher : vouchers) {
                VoucherDTO voucherDTO = modelMapper.map(voucher, VoucherDTO.class);
                voucherDTOs.add(voucherDTO);
            }

            ProfileUserDTO profileUserDTO = new ProfileUserDTO();
            profileUserDTO.setCustomer(customerDTO);
            profileUserDTO.setOrders(orderDTOs);
            profileUserDTO.setVouchers(voucherDTOs);

            return ResponseUtils.success(200, "Profile user", profileUserDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.fail(500, "Có lỗi xảy ra", null);

        }
    }

    public ResponseDTO updateImage(MultipartFile image) {
        User user = systemService.getUserLogin();

        // check image
        if (image != null) {
            if (user.getImage() != null) {
                fileService.deleteFireStore(user.getImage().getName());

                fileService.updateFile(image, ImageConst.CATEGORY_FOLDER, user.getImage());

            } else {
                File file = fileService.upload(image, ImageConst.CATEGORY_FOLDER);
                user.setImage(file);

            }
            User userSaved = userRepository.save(user);
            if (userSaved != null) {

                systemService.writeSystemLog(userSaved.getId(), userSaved.getName(), null);
                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                return ResponseUtils.fail(500, MessageConst.UPDATE_FAIL, null);

            }

        }
        return ResponseUtils.fail(200, "Uploadimage", null);

    }

    public ResponseDTO changeNumberPhone(LoginDTO loginDTO) {
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(loginDTO.getNumberPhone()).orElse(null);
        System.out.println("NMM0 " + loginDTO.getNumberPhone());
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

    public ResponseDTO changeProfile(LoginDTO loginDTO) {
        User user = userRepository.findByNumberPhoneAndActiveIsTrue(loginDTO.getNumberPhone()).orElse(null);
        System.out.println("NMM0 " + loginDTO.getNumberPhone());
        if (user != null) {
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


     public ResponseDTO updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        User user = userRepository.findByIdAndActiveIsTrue(profileUpdateDTO.getId()).orElse(null);
        if (user == null) {
            return ResponseUtils.fail(401, "người dùng không tồn tại hoặc bị khóa", null);
        }
        List<FieldError> errors = new ArrayList<>();
        // if(profileUpdateDTO.getEmail() != null) {
        //     User userEmail = userRepository.findByEmailAndActiveIsTrue(profileUpdateDTO.getEmail()).orElse(null);
        //      if (userEmail != null) {
        //     errors.add(new FieldError("ProfileUpdateDTO", "email", "Email đã được sử dụng"));
        // }
        // }
        // // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        // if (!errors.isEmpty()) {
         
        //     throw new ValidationException(errors);
        // }

     
        modelMapper.map(profileUpdateDTO, user);
        user.setUpdateAt(new Date());
        user.setUpdateBy(systemService.getUserLogin());
        User userSaved = userRepository.save(user);
        systemService.writeSystemLog(userSaved.getId(), userSaved.getNumberPhone(), null);

        return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

    }

    public ResponseDTO confirmOTPChangeProfile(ConfirmOtpDTO confirmOtpDTO) {

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

    public ResponseDTO confirmOTPChangePhone(ConfirmOtpDTO confirmOtpDTO) {
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
                 systemService.writeLoginLog(jwtToken, account.getUser());

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
            systemService.writeLoginLog(jwtToken, account.getUser());
            systemService.writeSystemLog(user.getId(), user.getName(), "Thay đổi số điện thoại");
            return ResponseUtils.success(200, "Đổi mật khẩu thành công", null);

        } else {
            return ResponseUtils.fail(500, "Đổi mật khẩu không thành công", null);

        }
    }

    public ResponseDTO collectVoucher(Integer voucherId) {

        Voucher voucher = voucherRepository.findByIdAndActiveIsTrue(voucherId).orElse(null);
        if (voucher == null) {
            System.out.println(voucherId);
            return ResponseUtils.fail(404, "Voucher không tồn tại", null);
        }

        User user = systemService.getUserLogin();

        UserVoucher userVoucher = userVoucherRepository.findBbyUserIdAndVoucherId(user.getId(),
                voucher.getId()).orElse(null);
        if (userVoucher != null) {
            if (userVoucher.getUseAt() != null) {
                return ResponseUtils.fail(400, "Voucher đã được thu thập và sử dụng", null);
            }
            return ResponseUtils.fail(400, "Voucher đã được thu thập", null);

        }
        userVoucher = new UserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);
        userVoucher.setCollectAt(new Date());

        userVoucherRepository.save(userVoucher);

        return ResponseUtils.success(200, "Thu thập voucher thành công", null);
    }

    public ResponseDTO getPermission() {
        User user = systemService.getUserLogin();
        String permission = rolePermissionService.getDataTokenRolePermisson(user.getRole().getId());
        String tokenPerssion = jwtUtil.generatePublicToken(permission);
        AuthenReponse authenReponse = new AuthenReponse();
        authenReponse.setTokenPermission(tokenPerssion);
        return ResponseUtils.success(200, "User permission", authenReponse);

    }

}
