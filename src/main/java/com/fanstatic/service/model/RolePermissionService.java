package com.fanstatic.service.model;

import com.fanstatic.config.constants.ApplicationConst;
import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.permissioin.*;
import com.fanstatic.dto.model.permissioin.conpact.FeaturePermssionCompactDTO;
import com.fanstatic.dto.model.permissioin.conpact.ManageFeatureCompactDTO;
import com.fanstatic.dto.model.permissioin.conpact.PermissionCompact;
import com.fanstatic.dto.model.role.FeaturePermissonDTO;
import com.fanstatic.dto.model.role.RoleRequestDTO;
import com.fanstatic.model.*;
import com.fanstatic.repository.*;
import com.fanstatic.service.system.SystemService;
import com.fanstatic.util.ResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobip.JSON;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private RoleService roleService;

    public ResponseDTO setRolePermission(SetRolePermissionDTO setRolePermissionDTO) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Role role = roleRepository.findByIdAndActiveIsTrue(setRolePermissionDTO.getRoleId()).orElse(null);
        if (role == null) {
            return ResponseUtils.fail(404, "Vai trò không tồn tại", null);
        }

        if (role.getId() == ApplicationConst.CUSTOMER_ROLE_ID || role.getId() == ApplicationConst.ADNIN_ROLE_ID) {
            return ResponseUtils.fail(500, "Vai trò này không được thay đổi", null);

        }

        List<Integer> featurePermissionIdsToAdd = setRolePermissionDTO.getFeaturePermissionsId();
        RolePermissionDTO rolePermissionDTO = (RolePermissionDTO) (getRolePermisson(setRolePermissionDTO.getRoleId())
                .getData());

        List<Integer> featurePermissionIdsInRole = rolePermissionDTO.getFeaturePermissions().stream()
                .map(FeaturePermissionDTO::getPermissions)
                .flatMap(List::stream)
                .map(PermissionDTO::getFeaturePermissionId)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> featurePermissionIdsToAddToDb = featurePermissionIdsToAdd.stream()
                .filter(id -> !featurePermissionIdsInRole.contains(id))
                .collect(Collectors.toList());

        // Tạo danh sách cần xóa khỏi csdl
        List<Integer> featurePermissionIdsToDeleteFromDb = featurePermissionIdsInRole.stream()
                .filter(id -> !featurePermissionIdsToAdd.contains(id))
                .collect(Collectors.toList());

        // xoa quyen

        try {

            for (Integer featurePermissionId : featurePermissionIdsToDeleteFromDb) {
                rolePermissionRepository.deleteByFeaturePermissionId(featurePermissionId,
                        setRolePermissionDTO.getRoleId());
            }
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);

            e.printStackTrace();
            return ResponseUtils.fail(500, "Gán quyền thất bại", null);
        }

        // kiem tra va gan quyen
        for (Integer featurePermissionId : featurePermissionIdsToAddToDb) {

            RolePermission rolePermission = new RolePermission();

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
            RolePermissionDTO rolePermissonDTO = new RolePermissionDTO();
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
            rolePermissonDTO.setRole(roleDTO);
            List<FeaturePermissionDTO> featurePermissionDTOs = new ArrayList<>();

            // kiem tra vong lap cac role permission
            List<String> addedFeatureId = new ArrayList<>();
            for (RolePermission rolePermission : rolePermissions) {
                FeaturePermission featurePermission = rolePermission.getFeaturePermission();
                ManagerFeature managerFeature = featurePermission.getManagerFeature();

                if (addedFeatureId.contains(managerFeature.getId())) {
                    continue;
                }
                addedFeatureId.add(managerFeature.getId());

                FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
                ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

                featurePermissionDTO.setManageFeature(manageFeatureDTO);

                // lay cac permission lien quan den feature
                List<FeaturePermission> featurePermissions = featurePermissionRepository
                        .findByManagerFeature(managerFeature);

                List<PermissionDTO> permissionDTOs = new ArrayList<>();

                for (FeaturePermission fePermission : featurePermissions) {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    if (fePermission.getPermission() == null) {
                        continue;
                    }
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
        RolePermissionDTO rolePermissonDTO = new RolePermissionDTO();
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        rolePermissonDTO.setRole(roleDTO);
        List<FeaturePermissionDTO> featurePermissionDTOs = new ArrayList<>();

        // kiem tra vong lap cac role permission
        List<String> addedFeatureId = new ArrayList<>();

        for (RolePermission rolePermission : rolePermissions) {
            FeaturePermission featurePermission = rolePermission.getFeaturePermission();
            ManagerFeature managerFeature = featurePermission.getManagerFeature();

            if (managerFeature == null) {
                continue;
            }

            if (addedFeatureId.contains(managerFeature.getId())) {
                continue;
            }
            addedFeatureId.add(managerFeature.getId());

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
                if (fePermission.getPermission() == null) {
                    continue;
                }
                permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                permissionDTO.setFeaturePermissionId(fePermission.getId());

                permissionDTOs.add(permissionDTO);
            }
            // featurePermissionDTO.setId(featurePermission.getId());
            featurePermissionDTO.setPermissions(permissionDTOs);
            featurePermissionDTOs.add(featurePermissionDTO);

        }

        List<FeaturePermissionDTO> featurePermissionDTO5L = new ArrayList<>();

        rolePermissonDTO.setFeaturePermissions(featurePermissionDTOs);
        return ResponseUtils.success(200, "Phân quyền vai trò", rolePermissonDTO);

    }

    public String getDataTokenRolePermisson(int roleId) {

        Role role = roleService.getById(roleId);
        if (role == null) {
            return null;
        }

        List<RolePermission> rolePermissions = role.getRolePermissions();

        // khoi tao reponse data
        RolePermissionDTO rolePermissonDTO = new RolePermissionDTO();
        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        rolePermissonDTO.setRole(roleDTO);
        List<FeaturePermssionCompactDTO> featurePermissionDTOs = new ArrayList<>();

        // kiem tra vong lap cac role permission
        List<String> addedFeatureId = new ArrayList<>();

        for (RolePermission rolePermission : rolePermissions) {
            FeaturePermission featurePermission = rolePermission.getFeaturePermission();
            ManagerFeature managerFeature = featurePermission.getManagerFeature();

            if (managerFeature == null) {
                continue;
            }

            if (addedFeatureId.contains(managerFeature.getId())) {
                continue;
            }
            addedFeatureId.add(managerFeature.getId());

            FeaturePermssionCompactDTO featurePermissionDTO = new FeaturePermssionCompactDTO();
            ManageFeatureCompactDTO manageFeatureDTO = new ManageFeatureCompactDTO();
            manageFeatureDTO.setId(managerFeature.getId());

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

            List<PermissionCompact> permissionDTOs = new ArrayList<>();

            for (FeaturePermission fePermission : featurePermissions) {
                PermissionCompact permissionDTO = new PermissionCompact();
                if (fePermission.getPermission() == null) {
                    continue;
                }
                permissionDTO.setId(fePermission.getPermission().getId());

                permissionDTOs.add(permissionDTO);
            }
            // featurePermissionDTO.setId(featurePermission.getId());
            featurePermissionDTO.setPermission(permissionDTOs);
            featurePermissionDTOs.add(featurePermissionDTO);

        }

        TokenPermissionDTO tokenPermissionDTO = new TokenPermissionDTO();
        tokenPermissionDTO.setFeaturePermissions(featurePermissionDTOs);
        tokenPermissionDTO.setRoleId(roleId);

        try {
            return objectMapper.writeValueAsString(tokenPermissionDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return "Not thing";
        }

    }

    public ResponseDTO getFeaturePermission() {
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();

        List<FeaturePermission> featurePermissions = featurePermissionRepository.findAll();

        List<FeaturePermissionDTO> featurePermissionDTOs = new ArrayList<>();

        // kiem tra vong lap cac role permission
        List<String> addedFeatureId = new ArrayList<>();

        for (FeaturePermission featurePermission : featurePermissions) {
            ManagerFeature managerFeature = featurePermission.getManagerFeature();

            if (managerFeature == null) {
                continue;
            }

            if (managerFeature.getId().equals("CUSTOMER_ORDER")){
                continue;
            }

            if (addedFeatureId.contains(managerFeature.getId())) {
                continue;
            }
            addedFeatureId.add(managerFeature.getId());

            FeaturePermissionDTO featurePermissionDTO = new FeaturePermissionDTO();
            ManageFeatureDTO manageFeatureDTO = modelMapper.map(managerFeature, ManageFeatureDTO.class);

            featurePermissionDTO.setManageFeature(manageFeatureDTO);

            // lay cac permission lien quan den feature
            List<FeaturePermission> featPermissions = featurePermissionRepository
                    .findByManagerFeature(managerFeature);

            List<PermissionDTO> permissionDTOs = new ArrayList<>();

            for (FeaturePermission fePermission : featPermissions) {
                PermissionDTO permissionDTO = new PermissionDTO();
                if (fePermission.getPermission() == null) {
                    continue;
                }
                permissionDTO = modelMapper.map(fePermission.getPermission(), PermissionDTO.class);
                permissionDTO.setFeaturePermissionId(fePermission.getId());

                permissionDTOs.add(permissionDTO);
            }
            // featurePermissionDTO.setId(featurePermission.getId());
            featurePermissionDTO.setPermissions(permissionDTOs);
            featurePermissionDTOs.add(featurePermissionDTO);

        }
        Collections.sort(featurePermissionDTOs, new FeaturePermissionComparator());

        List<ResponseDataDTO> datas = featurePermissionDTOs.stream()
                .map(featurePermissionDTO -> (ResponseDataDTO) featurePermissionDTO).collect(Collectors.toList());

        reponseListDataDTO.setDatas(datas);
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

    class FeaturePermissionComparator implements Comparator<FeaturePermissionDTO> {
        @Override
        public int compare(FeaturePermissionDTO dto1, FeaturePermissionDTO dto2) {
            // So sánh theo chiều dài của danh sách permissions
            int sizeComparison = Integer.compare(dto1.getPermissions().size(), dto2.getPermissions().size());

            // Ưu tiên danh sách có độ dài là 5
            if (dto1.getPermissions().size() == 5) {
                return -1; // Độ dài là 5 đến trước
            } else if (dto2.getPermissions().size() == 5) {
                return 1; // Độ dài là 5 đến trước
            }

            if (sizeComparison != 0) {
                return sizeComparison;
            }
            return 0;

        }
    }

}
