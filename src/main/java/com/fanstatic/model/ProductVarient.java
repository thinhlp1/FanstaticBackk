package com.fanstatic.model;

import java.math.BigInteger;
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
 * The persistent class for the product_varient database table.
 * 
 */

@Table(name = "product_varient")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVarient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String code;

	private Long price;

	// bi-directional many-to-one association to OrderItem
	@OneToMany(mappedBy = "productVarient")
	private List<OrderItem> orderItems;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to Size
	@ManyToOne
	private Size size;

	private Byte active;

	@Column(name = "out_of_stock")
	private Byte outOfStock;

	@OneToMany(mappedBy = "productVariant")
	private List<ProductImage> productImages;

	// bi-directional many-to-one association to SaleProduct
	@OneToMany(mappedBy = "productVarient")
	private List<SaleProduct> saleProducts;

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