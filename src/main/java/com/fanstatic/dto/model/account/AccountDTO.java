package com.fanstatic.dto.model.account;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO extends ResponseDataDTO{
	private int id;

	private byte active;

	private String numberPhone;

	private String password;

	private RoleDTO roleId;

}
