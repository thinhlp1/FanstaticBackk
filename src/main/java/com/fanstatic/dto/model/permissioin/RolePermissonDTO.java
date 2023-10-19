package com.fanstatic.dto.model.permissioin;

import java.util.List;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePermissonDTO extends ResponseDataDTO {
    
    private RoleDTO role;

    private List<FeaturePermissionDTO> featurePermissions;

}
