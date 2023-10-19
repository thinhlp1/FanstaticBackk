package com.fanstatic.dto.model.user;

import java.math.BigInteger;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailDTO extends ResponseDataDTO {
	private int id;

	private Date dateOfBirth;

	private String email;

	private String employeeCode;

	private String cccdCmnd;

	private String backCmmndUrl;

	private String frontCccdUrl;

	private String imageUrl;

	private byte gender;

	private String name;

	private String numberPhone;

	private String placeOrigin;

	private String placeResidence;

	private byte active;

	private BigInteger point;

	private RoleDTO role;

	private Date createAt;

	private UserCompactDTO createBy;

	private Date updateAt;

	private UserCompactDTO updateBy;

	private Date deleteAt;

	private UserCompactDTO deleteBy;

}
