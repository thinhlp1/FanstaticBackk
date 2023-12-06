package com.fanstatic.model;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * /**
 * The persistent class for the loginlog database table.
 * 
 */

@Entity
@Table(name = "loginlog")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loginlog {

	public final static String LOGIN = "LOGIN";
	public final static String LOGOUT = "LOGOUT";

	@Id
	private int id;

	@Column(name = "action")
	private String action;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "action_at")
	private Date actionAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logoutAt")
	private Date logoutAt;

	private String description;

	private String token;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}