package com.fanstatic.dto.model.permissioin;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetRolePermissionDTO {

    @NotNull(message = "Nhập vai trò")
    private int roleId;

    private List<Integer> featurePermissionsId;

}
