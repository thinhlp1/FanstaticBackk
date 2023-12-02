package com.fanstatic.dto.model.permissioin;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionDTO {
    private String id;

	private String description;

	private String name;

	private int featurePermissionId;
}
