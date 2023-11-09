package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.FieldError;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.config.constants.DataConst;
import com.fanstatic.config.constants.MessageConst;
import com.fanstatic.config.constants.RequestParamConst;
import com.fanstatic.config.exception.ValidationException;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import com.fanstatic.dto.model.role.RoleRequestDTO;
import com.fanstatic.model.Role;
import com.fanstatic.repository.AccountRepository;
import com.fanstatic.repository.RoleRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import com.twilio.rest.api.v2010.account.Application;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final SystemService systemService;
    private final RolePermissionService rolePermissionService;
    private final PlatformTransactionManager transactionManager;
    private final AccountRepository accountRepository;

    public ResponseDTO create(RoleRequestDTO roleRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        List<FieldError> errors = new ArrayList<>();
        if (roleRepository.findByCodeAndActiveIsTrue(roleRequestDTO.getCode()).isPresent()) {
            errors.add(new FieldError("roleRequestDTO", "code", "Code đã tồn tại"));
        }

        // if
        // (roleRepository.findByNameAndActiveIsTrue(roleRequestDTO.getName()).isPresent())
        // {
        // errors.add(new FieldError("roleRequestDTO", "name", "Tên vai trò đã tồn
        // tại"));
        // }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Role role = modelMapper.map(roleRequestDTO, Role.class);

        role.setActive(DataConst.ACTIVE_TRUE);
        role.setCreateAt(new Date());
        role.setCreateBy(systemService.getUserLogin());

        Role roleSaved = roleRepository.saveAndFlush(role);

        if (roleSaved != null) {

            ResponseDTO setiRolePermission = rolePermissionService.setRolePermission(roleRequestDTO);
            if (setiRolePermission.isSuccess()) {
                systemService.writeSystemLog(role.getId(), role.getName(), null);

                transactionManager.commit(transactionStatus);

                return ResponseUtils.success(200, MessageConst.ADD_SUCCESS, null);

            } else {
                transactionManager.rollback(transactionStatus);

                return ResponseUtils.fail(setiRolePermission.getStatusCode(), setiRolePermission.getMessage(), null);

            }

        }
        return ResponseUtils.fail(500, MessageConst.ADD_FAIL, null);

    }

    public ResponseDTO update(RoleRequestDTO roleRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Role role = roleRepository.findById(roleRequestDTO.getId()).orElse(null);

        if (role == null) {

            return ResponseUtils.fail(404, "Vai trò không tồn tại", null);

        }

        List<FieldError> errors = new ArrayList<>();
        if (roleRepository.findByCodeAndActiveIsTrueAndIdNot(role.getCode(), role.getId()).isPresent()) {

            errors.add(new FieldError("roleRequestDTO", "id", "Id đã tồn tại"));
        }

        // if (roleRepository.findByNameAndActiveIsTrueAndIdNot(role.getName(),
        // role.getId()).isPresent()) {

        // errors.add(new FieldError("roleRequestDTO", "name", "Tên vai trò đã tồn
        // tại"));
        // }

        // Nếu có lỗi, ném ra một lượt với danh sách lỗi
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        modelMapper.map(roleRequestDTO, role);
        role.setUpdateAt(new Date());
        role.setUpdateBy(systemService.getUserLogin());

        Role roleSaved = roleRepository.save(role);

        if (roleSaved != null) {
            ResponseDTO setiRolePermission = rolePermissionService.setRolePermission(roleRequestDTO);
            if (setiRolePermission.isSuccess()) {
                systemService.writeSystemLog(role.getId(), role.getName(), null);

                transactionManager.commit(transactionStatus);

                return ResponseUtils.success(200, MessageConst.UPDATE_SUCCESS, null);

            } else {
                transactionManager.rollback(transactionStatus);

                return ResponseUtils.fail(setiRolePermission.getStatusCode(), setiRolePermission.getMessage(), null);

            }

        }
        return ResponseUtils.fail(500, "Cập nhật thất bại", null);
    }

    public ResponseDTO delete(int id) {
        Role role = roleRepository.findByIdAndActiveIsTrue(id).orElse(null);
        if (role == null) {
            return ResponseUtils.fail(404, "Vai trò không tồn tại", null);
        }

        switch (id) {
            case ApplicationConst.CUSTOMER_ROLE_ID:
                return ResponseUtils.fail(500, "Vai trò này là khách hàng. Không được xóa", null);
            case ApplicationConst.CASHIER_ROLE_ID:
                return ResponseUtils.fail(500, "Vai trò này là khách hàng. Không được xóa", null);
            case ApplicationConst.ADNIN_ROLE_ID:
                return ResponseUtils.fail(500, "Vai trò này là khách hàng. Không được xóa", null);
            default:
                break;
        }

        role.setActive(DataConst.ACTIVE_FALSE);
        role.setDeleteAt(new Date());
        role.setDeleteBy(systemService.getUserLogin());

        // Xoa cac account co role vua moi xoa

        Role roleSaved = roleRepository.save(role);

        if (roleSaved != null) {
            accountRepository.updateActiveByRoleId(DataConst.ACTIVE_FALSE, role.getId());

            systemService.writeSystemLog(role.getId(), role.getName(), null);
            return ResponseUtils.success(200, MessageConst.DELETE_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.DELETE_FAIL, null);

        }

    }

    public ResponseDTO restore(int id) {
        Role role = roleRepository.findByIdAndActiveIsFalse(id).orElse(null);
        if (role == null) {
            return ResponseUtils.fail(404, "Vai trò không tồn tại", null);
        }
        // check if any have same id
        int countCheck = roleRepository.countByCode(role.getCode());
        if (countCheck > 0) {
            String newRoleCode = role.getCode() + "_" + (countCheck - 1);
            role.setCode(newRoleCode);
        }

        role.setActive(DataConst.ACTIVE_TRUE);
        role.setUpdateAt(new Date());
        role.setUpdateBy(systemService.getUserLogin());

        // restore account

        Role roleSaved = roleRepository.save(role);

        if (roleSaved != null) {
            accountRepository.updateActiveByRoleId(DataConst.ACTIVE_TRUE, role.getId());

            systemService.writeSystemLog(role.getId(), role.getName(), null);
            return ResponseUtils.success(200, MessageConst.RESTORE_SUCCESS, null);

        } else {
            return ResponseUtils.fail(500, MessageConst.RESTORE_FAIL, null);

        }

    }

    public ResponseDTO show(int active) {
        List<Role> roles = new ArrayList<>();

        switch (active) {
            case RequestParamConst.ACTIVE_ALL:
                roles = roleRepository.findAll();
                break;
            case RequestParamConst.ACTIVE_TRUE:
                roles = roleRepository.findAllByActiveIsTrue().orElse(roles);
                break;
            case RequestParamConst.ACTIVE_FALSE:
                roles = roleRepository.findAllByActiveIsFalse().orElse(roles);
                break;
            default:
                roles = roleRepository.findAll();
                break;
        }
        List<ResponseDataDTO> roleDTOS = new ArrayList<>();

        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            modelMapper.map(role, roleDTO);

            roleDTOS.add(roleDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(roleDTOS);
        return ResponseUtils.success(200, "Danh sách vai trò", reponseListDataDTO);
    }

    public RoleDTO getDTOById(int id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            return modelMapper.map(role, RoleDTO.class);
        } else {
            return null;
        }
    }

    public Role getById(int id) {
        return roleRepository.findById(id).orElse(null);

    }
}
