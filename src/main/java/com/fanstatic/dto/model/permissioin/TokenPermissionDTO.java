package com.fanstatic.dto.model.permissioin;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.conpact.FeaturePermssionCompactDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenPermissionDTO extends ResponseDataDTO {
    private List<FeaturePermssionCompactDTO> featurePermissions;
    private Integer roleId;
}
