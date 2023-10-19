package com.fanstatic.model;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 
 * 
 * /**
 * The persistent class for the voucher database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Voucher  {
	
	@Id
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_at")
	private Date endAt;

	private String name;

	private int percent;

	@Column(name = "price_condition")
	private BigInteger priceCondition;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_at")
	private Date startAt;

	private BigInteger value;

	@Column(name = "voucher_code")
	private String voucherCode;

	// bi-directional many-to-one association to OrderVoucher
	@OneToMany(mappedBy = "voucher")
	private List<OrderVoucher> orderVouchers;

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