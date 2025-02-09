package com.fanstatic.model;

import java.math.BigInteger;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	private byte active;

	@Column(name = "cccd_cmnd")
	private String cccdCmnd;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	private String email;

	@Column(name = "employee_code")
	private String employeeCode;

	private byte gender;

	private String name;

	@Column(name = "number_phone")
	private String numberPhone;

	@Column(name = "place_origin")
	private String placeOrigin;

	@Column(name = "place_residence")
	private String placeResidence;

	private Long point;


	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;

	// bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@OneToOne(mappedBy = "user")
	private Account account;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_at")
	private Date updateAt;

	@OneToOne
	@JoinColumn(name = "update_by")
	private User updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;

	@OneToOne
	@JoinColumn(name = "create_by")
	private User createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_at")
	private Date deleteAt;

	@OneToOne
	@JoinColumn(name = "delete_by")
	private User deleteBy;


	@Override
	public String toString() {
		return "Not to String";
	}

	@Override
	public int hashCode() {
		return -1;
	}

}