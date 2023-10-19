package com.fanstatic.dto.model.customer;

import java.math.BigInteger;

import java.util.Date;

import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.model.permissioin.RoleDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO extends ResponseDataDTO {
	private int id;

	private Date dateOfBirth;

	private String email;

	private String imageUrl;

	private byte gender;

	private String name;

	private String numberPhone;

	private byte active;

	private BigInteger point;

	private RoleDTO role;
}
