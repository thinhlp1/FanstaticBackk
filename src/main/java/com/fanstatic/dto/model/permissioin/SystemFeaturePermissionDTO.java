package com.fanstatic.dto.model.permissioin;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SystemFeaturePermissionDTO {
        private List<FeaturePermissionDTO> featurePermissions;

}
