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
 * The persistent class for the combo_product database table.
 * 
 */


@Entity
@Data
@Builder
@Table(name = "combo_product")
@AllArgsConstructor
@NoArgsConstructor
public class ComboProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private byte active;

	@ManyToOne
	@JoinColumn(name = "description")
	private File description;

	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;

	private String name;

	private String code;

	private Long price;

	@Column(name = "out_of_stock")
	private byte outOfStock;

	// bi-directional many-to-one association to Category
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	// bi-directional many-to-one association to ComboProductDetail
	@OneToMany(mappedBy = "comboProduct")
	private List<ComboProductDetail> comboProductDetails;

	// bi-directional many-to-one association to HotProduct
	@OneToMany(mappedBy = "comboProduct")
	private List<HotProduct> hotProducts;

	// bi-directional many-to-one association to ProductRating
	@OneToMany(mappedBy = "comboProduct")
	private List<ProductRating> productRatings;

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