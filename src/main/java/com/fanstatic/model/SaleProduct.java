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

@Table(name = "sale_product")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleProduct  {
	
	@Id
	private int id;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to ProductVarient
	@ManyToOne
	@JoinColumn(name = "product_variant_id")
	private ProductVarient productVarient;

	// bi-directional many-to-one association to SaleEvent
	@ManyToOne
	@JoinColumn(name = "sale_event_id")
	private SaleEvent saleEvent;

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