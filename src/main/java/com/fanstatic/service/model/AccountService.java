package com.fanstatic.service.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fanstatic.config.constants.DataConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.model.account.AccountRequestDTO;
import com.fanstatic.model.Account;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final SystemService systemService;

    @Autowired
    @Lazy
    private RoleService roleService;
    @Autowired
    @Lazy
    private UserService userService;

    public ResponseDTO create(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        account.setNumberPhone(accountRequestDTO.getNumberPhone());

        account.setUser(userService.getById(accountRequestDTO.getUserId()));
        account.setRole(roleService.getById(accountRequestDTO.getRoleId()));
        account.setActive(DataConst.ACTIVE_TRUE);
        account.setCreateAt(new Date());
        // TODO thay user 1 thanh user dang dang nhap vao he thong
        account.setCreateBy(systemService.getUserLogin()); // user 1 is admin thinhlp

        account = accountRepository.save(account);
        Account accountSave = accountRepository.save(account);
        if (accountSave != null) {
            return ResponseUtils.success(200, "Tạo  thành công", null);

        } else {
            return ResponseUtils.fail(500, "Tạo thất bại", null);

        }
    }

    public ResponseDTO update(AccountRequestDTO accountRequestDTO) {
        Account account = accountRepository.findByUserIdAndActiveIsTrue(accountRequestDTO.getUserId()).orElse(null);
        account.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        account.setNumberPhone(accountRequestDTO.getNumberPhone());
        account.setRole(roleService.getById(accountRequestDTO.getRoleId()));
        account.setActive(DataConst.ACTIVE_TRUE);
        account.setUpdateAt(new Date());
        // TODO thay user 1 thanh user dang dang nhap vao he thong
        account.setUpdateBy(systemService.getUserLogin()); // user 1 is admin thinhlp

        account = accountRepository.save(account);
        Account accountSave = accountRepository.save(account);
        if (accountSave != null) {
            return ResponseUtils.success(200, "Cập nhật thành công", null);

        } else {
            return ResponseUtils.fail(500, "Cập nhật thành công", null);

        }
    }

    public ResponseDTO delete(Integer userId) {
        Account account = accountRepository.findByUserId(userId).orElse(null);
        if (account == null) {
            return ResponseUtils.fail(500, "Account không tồn tại", null);

        }


        account.setActive(DataConst.ACTIVE_FALSE);
        account.setDeleteAt(new Date());
        account.setDeleteBy(systemService.getUserLogin());
        Account accountSave = accountRepository.save(account);
        if (accountSave != null) {
            return ResponseUtils.success(200, "Xóa thành công", null);

        } else {
            return ResponseUtils.fail(500, "Xóa thất bại", null);

        }

    }

    public ResponseDTO restore(Integer userId) {
        Account account = accountRepository.findByUserId(userId).orElse(null);
        if (account == null) {
            return ResponseUtils.fail(500, "Account không tồn tại", null);

        }

        account.setActive(DataConst.ACTIVE_TRUE);
        account.setUpdateAt(new Date());
        account.setUpdateBy(systemService.getUserLogin());
        Account accountSave = accountRepository.save(account);
        if (accountSave != null) {
            return ResponseUtils.success(200, "Khôi phục thành công", null);

        } else {
            return ResponseUtils.fail(500, "Khôi phục thất bại", null);

        }

    }

    public Account getByUserID(Integer userId) {
        return accountRepository.findByUserId(userId).orElse(null);
    }
}
