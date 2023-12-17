package com.fanstatic.dto.model.permissioin;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO extends ResponseDataDTO {
	private String id;

	private String code;

	private byte active;

	private String name;

	private String description;

}
