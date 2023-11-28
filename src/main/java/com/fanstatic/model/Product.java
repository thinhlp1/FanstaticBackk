package com.fanstatic.model;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

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
 * The persistent class for the product database table.
 * 
 */
@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private Byte active;

	private String code;

	private String name;

	private Long price;

	@Column(name = "out_of_stock")
	private Byte outOfStock;

	// // bi-directional many-to-one association to ComboProductDetail
	// @OneToMany(mappedBy = "product")
	// private List<ComboProductDetail> comboProductDetails;

	// bi-directional many-to-one association to HotProduct
	@OneToMany(mappedBy = "product")
	private List<HotProduct> hotProducts;

	// bi-directional many-to-one association to OrderItem
	@OneToMany(mappedBy = "product")
	private List<OrderItem> orderItems;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "description")
	private File description;

	@OneToMany(mappedBy = "product")
	private List<ProductImage> images;

	@OneToMany(mappedBy = "product")
	private List<ProductCategory> productCategories;

	// bi-directional many-to-one association to ProductRating
	@OneToMany(mappedBy = "product")
	private List<ProductRating> productRatings;

	// bi-directional many-to-one association to ProductVarient
	@OneToMany(mappedBy = "product")
	private List<ProductVarient> productVarients;

	// bi-directional many-to-one association to SaleProduct
	@OneToMany(mappedBy = "product")
	private List<SaleProduct> saleProducts;

	@OneToMany(mappedBy = "product")
	private List<ProductOption> productOptions;

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