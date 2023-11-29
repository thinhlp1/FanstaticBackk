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
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String note;

	private Integer priority;

	private Integer quantity;

	@Column(name = "item_price")
	private Long itemPrice;

	@Column(name = "quantity_completed")
	private Integer quantityCompleted;

	// bi-directional many-to-one association to Order
	@ManyToOne()
	@JoinColumn(name = "order_id")
	private Order order;

	// bi-directional many-to-one association to Product
	@ManyToOne()
	private Product product;

	// bi-directional many-to-one association to ProductVarient
	@ManyToOne()
	@JoinColumn(name = "product_variant_id")
	private ProductVarient productVarient;

	@ManyToOne()
	@JoinColumn(name = "combo_id")
	private ComboProduct comboProduct;
	// bi-directional many-to-one association to Status
	@ManyToOne
	private Status status;

	// bi-directional many-to-one association to OrderItemOption
	@OneToMany(mappedBy = "orderItem")
	private List<OrderItemOption> orderItemOptions;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_at")
	private Date updateAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "update_by")
	private User updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by")
	private User createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_at")
	private Date deleteAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delete_by")
	private User deleteBy;

}