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
 * /**
 * The persistent class for the order database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;

	@Column(name = "cancel_reason")
	private String cancelReason;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private User customer;

	@ManyToOne
	@JoinColumn(name = "employee_confirmed")
	private User employeeConfirmed;

	private String note;

	@Column(name = "order_type")
	private String orderType;

	private BigInteger total;

	// bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	// bi-directional many-to-one association to OrderExtraPortion
	@OneToMany(mappedBy = "order")
	private List<OrderExtraPortion> orderExtraPortions;

	// bi-directional many-to-one association to OrderItem
	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems;

	// bi-directional many-to-one association to OrderSurcharge
	@OneToMany(mappedBy = "order")
	private List<OrderSurcharge> orderSurcharges;

	// bi-directional many-to-one association to OrderTable
	@OneToMany(mappedBy = "order")
	private List<OrderTable> orderTables;

	// bi-directional many-to-one association to OrderVoucher
	@ManyToOne
	@JoinColumn(name = "voucher_id")
	private Voucher voucher;

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