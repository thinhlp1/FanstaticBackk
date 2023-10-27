package com.fanstatic.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.permissioin.FeaturePermissionDTO;
import com.fanstatic.dto.model.permissioin.ManageFeatureDTO;
import com.fanstatic.dto.model.permissioin.PermissionDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import com.fanstatic.dto.model.permissioin.RolePermissonDTO;
import com.fanstatic.dto.model.role.FeaturePermissonDTO;
import com.fanstatic.dto.model.role.RoleRequestDTO;
import com.fanstatic.model.Account;
import com.fanstatic.model.FeaturePermission;
import com.fanstatic.model.ManagerFeature;
import com.fanstatic.model.Permission;
import com.fanstatic.model.Role;
import com.fanstatic.model.RolePermission;
import com.fanstatic.repository.FeaturePermissionRepository;
import com.fanstatic.repository.ManagerFeatureRepository;
import com.fanstatic.repository.PermissionRepository;
import com.fanstatic.repository.RolePermissionRepository;
import com.fanstatic.repository.RoleRepository;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    private final ModelMapper modelMapper;
    private final RolePermissionRepository rolePermissionRepository;
    private final FeaturePermissionRepository featurePermissionRepository;
    private final ManagerFeatureRepository managerFeatureRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final SystemService systemService;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    @Lazy
    private RoleService roleService;

    public ResponseDTO setRolePermission(RoleRequestDTO roleRequestDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        // kiem tra xem đang update hay create, neu update xoa các role permission cu
        rolePermissionRepository.deleteByRoleId(roleRequestDTO.getId());

        // kiem tra va gan quyen
        for (FeaturePermissonDTO featurePermissonDTO : roleRequestDTO.getFeaturePermissions()) {

            for (String permissionId : featurePermissonDTO.getPermissionIds()) {
                RolePermission rolePermission = new RolePermission();
                Role role = roleRepository.findByCodeAndActiveIsTrue(roleRequestDTO.getCode()).orElse(null);

                if (role == null) {
                    transactionManager.rollback(transactionStatus);

                    return ResponseUtils.fail(404, "Vai trò không tồn tại", null);

                }

                ManagerFeature managerFeature = managerFeatureRepository.findById(featurePermissonDTO.getFeatureId())
                        .orElse(null);
                if (managerFeature == null) {
                    transactionManager.rollback(transactionStatus);

                    return ResponseUtils.fail(404, "Chức năng không tồn tại", null);

                }

                Permission permission = permissionRepository.findById(permissionId).orElse(null);
                if (permission == null) {
                    transactionManager.rollback(transactionStatus);

                    return ResponseUtils.fail(404, "Quyền này không tồn tại", null);

                }
                FeaturePermission featurePermission = featurePermissionRepository
                        .findByManagerFeatureAndPermission(managerFeature, permission)
                        .orElse(null);
                if (featurePermission == null) {
                    transactionManager.rollback(transactionStatus);

                    return ResponseUtils.fail(404, "Quyền không tồn tại", null);

                }

                rolePermission.setFeaturePermission(featurePermission);
                rolePermission.setCreateAt(new Date());
                rolePermission.setCreateBy(systemService.getUserLogin());
                rolePermission.setRole(role);

                try {

                    rolePermissionRepository.save(rolePermission);
                } catch (Exception e) {
                    transactionManager.rollback(transactionStatus);

                    e.printStackTrace();
                    return ResponseUtils.fail(500, "Gán quyền thất bại", null);
                }
            }
        }
        transactionManager.commit(transactionStatus);

        return ResponseUtils.success(200, "Gán quyền thành công", null);
    }

    public ResponseDTO getRolePermisson() {
        // lay thong tin user neu dang co dang nhap
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Account account = (Account) authentication.getPrincipal();

            Role role = roleService.getById(account.getRole().getId());
            List<RolePermission> rolePermissions = role.getRolePermissions();

            // khoi tao reponse data
            RolePermissonDTO rolePermissonDTO = new RolePermissonDTO();
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
            rolePermissonDTO.setRole(roleDTO);
            List<FeaturePermissionDTO> featurePermissionDTOs = new ArrayList<>();

            // kiem tra vong lap cac role permission
            String addedFeatureId = "";
            for (RolePermission rolePermission : rolePermissions) {
                FeaturePermission featurePermission = rolePermission.getFeaturePermission();
                ManagerFeature managerFeature = featurePermission.getManagerFeature();

                if (managerFeature.getId().equals(addedFeatureId)) {
                    continue;
                }
                addedFeatureId = managerFeature.getId();

                FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
                ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

                featurePermissionDTO.setManageFeature(manageFeatureDTO);

                // lay cac permission lien quan den feature
                List<FeaturePermission> featurePermissions = featurePermissionRepository
                        .findByManagerFeature(managerFeature);

                List<PermissionDTO> permissionDTOs = new ArrayList<>();

                for (FeaturePermission fePermission : featurePermissions) {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                    permissionDTOs.add(permissionDTO);
                }

                featurePermissionDTO.setPermissions(permissionDTOs);
                featurePermissionDTOs.add(featurePermissionDTO);

            }

            rolePermissonDTO.setFeaturePermissions(featurePermissionDTOs);
            return ResponseUtils.success(200, "Quyền của người dùng hiện tại", rolePermissonDTO);

        }
        return ResponseUtils.fail(500, "Không thể xem phân quyền", null);

    }

    public ResponseDTO getRolePermisson(int roleId) {

        Role role = roleService.getById(roleId);
        if (role == null) {
            return ResponseUtils.fail(500, "Vai trò không hợp lệ", null);
        }

        List<RolePermission> rolePermissions = role.getRolePermissions();

        // khoi tao reponse data
        RolePermissonDTO rolePermissonDTO = new RolePermissonDTO();
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        rolePermissonDTO.setRole(roleDTO);
        List<FeaturePermissionDTO> featurePermissionDTOs = new ArrayList<>();

        // kiem tra vong lap cac role permission
        String addedFeatureId = "";
        for (RolePermission rolePermission : rolePermissions) {
            FeaturePermission featurePermission = rolePermission.getFeaturePermission();
            ManagerFeature managerFeature = featurePermission.getManagerFeature();

            if (managerFeature.getId().equals(addedFeatureId)) {
                continue;
            }
            addedFeatureId = managerFeature.getId();

            FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
            ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

            featurePermissionDTO.setManageFeature(manageFeatureDTO);

            // lay cac permission lien quan den feature
            List<FeaturePermission> featurePermissions = featurePermissionRepository
                    .findByManagerFeature(managerFeature);

            List<PermissionDTO> permissionDTOs = new ArrayList<>();

            for (FeaturePermission fePermission : featurePermissions) {
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                permissionDTOs.add(permissionDTO);
            }
            // featurePermissionDTO.setId(featurePermission.getId());
            featurePermissionDTO.setPermissions(permissionDTOs);
            featurePermissionDTOs.add(featurePermissionDTO);

        }

        rolePermissonDTO.setFeaturePermissions(featurePermissionDTOs);
        return ResponseUtils.success(200, "Phân quyền vai trò", rolePermissonDTO);

    }

    public ResponseDTO getFeaturePermission() {
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        List<FeaturePermission> featurePermissions = featurePermissionRepository.findAll();

        List<ResponseDataDTO> featurePermissionDTOs = new ArrayList<>();

        // kiem tra vong lap cac role permission
        String addedFeatureId = "";
        for (FeaturePermission featurePermission : featurePermissions) {
            ManagerFeature managerFeature = featurePermission.getManagerFeature();

            if (managerFeature.getId().equals(addedFeatureId)) {
                continue;
            }
            addedFeatureId = managerFeature.getId();

            FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
            ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

            featurePermissionDTO.setManageFeature(manageFeatureDTO);

            // lay cac permission lien quan den feature
            List<FeaturePermission> featPermissions = featurePermissionRepository
                    .findByManagerFeature(managerFeature);

            List<PermissionDTO> permissionDTOs = new ArrayList<>();

            for (FeaturePermission fePermission : featPermissions) {
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                permissionDTOs.add(permissionDTO);
            }
            // featurePermissionDTO.setId(featurePermission.getId());
            featurePermissionDTO.setPermissions(permissionDTOs);
            featurePermissionDTOs.add(featurePermissionDTO);

        }
        reponseListDataDTO.setDatas(featurePermissionDTOs);
        reponseListDataDTO.setNameList("Hệ thống phân quyền");
        return ResponseUtils.success(200, "Hệ thống phân quyền", reponseListDataDTO);

    }

    public boolean checkUserRolePermission(int roleId, String manageFeatureId, String permissionId) {

        long count = rolePermissionRepository.countByRoleAndManageFeatureAndPermission(roleId, manageFeatureId,
                permissionId);

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }
}
