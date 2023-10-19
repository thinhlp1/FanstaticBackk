package com.fanstatic.service.authentication;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.auth.AuthenReponse;
import com.fanstatic.dto.auth.CreateAccountDTO;
import com.fanstatic.dto.auth.LoginDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.Role;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.repository.RoleRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.JwtUtil;
import com.fanstatic.util.ResponseUtils;
import com.fanstatic.util.SessionUtils;

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
    private final SessionUtils sessionUtils;

    public ResponseDTO login(LoginDTO loginDTO) {
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
            sessionUtils.set("token", jwtToken);
            systemService.writeLoginLog(jwtToken, account.getUser());
            // return authenReponse;
            return ResponseUtils.success(200, "Đăng nhập thành công", authenReponse);

        } catch (Exception e) {
            return ResponseUtils.fail(500, "Có lỗi xảy ra khi đăng nhập", null);

        }
    }

    public ResponseDTO createAccount(CreateAccountDTO createAccountDTO) {
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(createAccountDTO.getPassword()));
        account.setNumberPhone(createAccountDTO.getNumberPhone());

        Role role = roleRepository.findById(createAccountDTO.getRoleId()).orElse(null);
        account.setRole(role);
        account.setActive((byte) 1);
        account.setCreateAt(new Date());

        Account accountSave = accountRepository.save(account);
        if (accountSave == null) {
            return ResponseUtils.success(500, "Tạo thất bại", null);

        } else {
            return ResponseUtils.fail(200, "Tạo thành công", null);

        }

    }
}