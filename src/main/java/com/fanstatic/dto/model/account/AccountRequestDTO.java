package com.fanstatic.dto.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

	private int id;

    private String numberPhone;

	private String password;

	private int roleId;

	private int userId;


	public AccountRequestDTO(String numberPhone, String password, int roleId, int userId) {
		this.numberPhone = numberPhone;
		this.password = password;
		this.roleId = roleId;
		this.userId = userId;
	}
	
}
