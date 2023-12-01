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
 * /**
 * The persistent class for the sale_product database table.
 * 
 */


@Entity
@Data
@Table(name = "sale_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	private byte active;
	// bi-directional many-to-one association to ProductVarient
	@ManyToOne
	@JoinColumn(name = "product_variant_id")
	private ProductVarient productVarient;

	@ManyToOne
	@JoinColumn(name = "combo_product_id")
	private ComboProduct comboProduct;

	// bi-directional many-to-one association to SaleEvent
	@ManyToOne
	@JoinColumn(name = "sale_event_id")
	private SaleEvent saleEvent;

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