package com.fanstatic.dto.model.permissioin.conpact;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeaturePermssionCompactDTO {
    private ManageFeatureCompactDTO manageFeature;

    private List<PermissionCompact> permission;
}
