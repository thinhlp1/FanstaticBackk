package com.fanstatic.service.model;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.permissioin.*;
import com.fanstatic.dto.model.role.FeaturePermissonDTO;
import com.fanstatic.dto.model.role.RoleRequestDTO;
import com.fanstatic.model.*;
import com.fanstatic.repository.*;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        for (Integer featurePermissionId : roleRequestDTO.getFeaturePermissionsId()) {

            RolePermission rolePermission = new RolePermission();
            Role role = roleRepository.findByCodeAndActiveIsTrue(roleRequestDTO.getCode()).orElse(null);

            if (role == null) {
                transactionManager.rollback(transactionStatus);

                return ResponseUtils.fail(404, "Vai trò không tồn tại", null);

            }

            FeaturePermission featurePermission = featurePermissionRepository
                    .findById(featurePermissionId)
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

            if (managerFeature == null) {
                continue;
            }

            if (managerFeature.getId().equals(addedFeatureId)) {
                continue;
            }
            addedFeatureId = managerFeature.getId();

            FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
            ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

            featurePermissionDTO.setManageFeature(manageFeatureDTO);

            // lay cac permission lien quan den feature
            // List<FeaturePermission> featurePermissions = featurePermissionRepository
            // .findByManagerFeature(managerFeature);

            List<FeaturePermission> featurePermissions = new ArrayList<>();
            for (RolePermission rolePermission2 : rolePermissions) {
                if (rolePermission2.getFeaturePermission().getManagerFeature().getId().equals(managerFeature.getId())) {
                    featurePermissions.add(rolePermission2.getFeaturePermission());

                }
            }

            List<PermissionDTO> permissionDTOs = new ArrayList<>();

            for (FeaturePermission fePermission : featurePermissions) {
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                permissionDTO.setFeaturePermissionId(fePermission.getId());

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
                permissionDTO.setFeaturePermissionId(fePermission.getId());

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
