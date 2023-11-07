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
 * 
 * 
 * /**
 * The persistent class for the order_item database table.
 * 
 */

@Table(name = "order_item")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String note;

	private int priority;

	private int quantity;

	// bi-directional many-to-one association to Order
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private Order order;

	// bi-directional many-to-one association to Product
	@ManyToOne(cascade = CascadeType.ALL)
	private Product product;

	// bi-directional many-to-one association to ProductVarient
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_variant_id")
	private ProductVarient productVarient;

	// bi-directional many-to-one association to Status
	@ManyToOne
	private Status status;

	// bi-directional many-to-one association to OrderItemOption
	@OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
	private List<OrderItemOption> orderItemOptions;

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