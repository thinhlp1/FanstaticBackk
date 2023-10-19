package com.fanstatic.model;


import java.math.BigInteger;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**

/**
 * The persistent class for the bill database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill  {
	
	@Id
	@Column(name="bill_id")
	private int billId;

	private int id;

	private BigInteger point;

	@Column(name="receive_money")
	private BigInteger receiveMoney;

	private BigInteger tip;

	private BigInteger total;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_at")
	private Date updateAt;

	@OneToOne
	@JoinColumn(name="update_by")
	private User updateBy;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_at")
	private Date createAt;
	
	@OneToOne
	@JoinColumn(name="create_by")
	private User createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delete_at")
	private Date deleteAt;

	@OneToOne
	@JoinColumn(name="delete_by")
	private User deleteBy;

	//bi-directional many-to-one association to PaymentMethod
	@ManyToOne
	@JoinColumn(name="payment_method_id")
	private PaymentMethod paymentMethod;

	

}