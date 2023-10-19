package com.fanstatic.model;


import java.math.BigInteger;
import java.sql.Time;
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
 * The persistent class for the shift_handover database table.
 * 
 */

@Table(name = "shift_handover")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftHandover  {
	
	@Id
	private int id;

	@Column(name = "cash_handover")
	private BigInteger cashHandover;

	@Column(name = "end_shift_cash")
	private BigInteger endShiftCash;

	@Column(name = "end_shift_time")
	private Time endShiftTime;

	@Lob
	private String note;

	@Column(name = "start_shift_cash")
	private BigInteger startShiftCash;

	@Column(name = "start_shift_time")
	private Time startShiftTime;

	// bi-directional many-to-one association to RegisterShift
	@ManyToOne
	@JoinColumn(name = "register_id")
	private RegisterShift registerShift;

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