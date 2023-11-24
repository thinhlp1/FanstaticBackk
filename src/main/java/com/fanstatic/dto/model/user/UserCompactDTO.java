package com.fanstatic.dto.model.user;

import java.math.BigInteger;

import java.util.Date;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCompactDTO extends ResponseDataDTO {
	private int id;

	private String employeeCode;

	private String name;

	private RoleDTO role;

}
