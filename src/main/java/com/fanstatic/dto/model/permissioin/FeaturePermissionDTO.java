package com.fanstatic.dto.model.permissioin;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeaturePermissionDTO extends ResponseDataDTO {
    private ManageFeatureDTO manageFeature;
    private List<PermissionDTO> permissions;
}
