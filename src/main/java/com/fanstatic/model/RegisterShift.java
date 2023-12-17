package com.fanstatic.model;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * /**
 * The persistent class for the register_shift database table.
 * 
 */

@Table(name = "register_shift")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterShift  {
	
	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name = "day_work")
	private Date dayWork;

	@JoinColumn(name = "employee_id")
	@ManyToOne
	private User employee;

	@JoinColumn(name = "status_id")
	@ManyToOne
	private Status status;

	// bi-directional many-to-one association to Shift
	@ManyToOne
	private Shift shift;

	// bi-directional many-to-one association to ShiftHandover
	// @OneToMany(mappedBy = "registerShift")
	// private List<ShiftHandover> shiftHandovers;

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
}