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
 * The persistent class for the combo_product_details database table.
 * 
 */

@Table(name = "combo_product_details")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComboProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// bi-directional many-to-one association to ComboProduct
	@ManyToOne
	@JoinColumn(name = "combo_id")
	private ComboProduct comboProduct;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	@ManyToOne
	@JoinColumn(name = "product_varient_id")
	private ProductVarient productVarient;

	@ManyToOne
	@JoinColumn(name = "extra_portion_id")
	private ExtraPortion extraPortion;

	private Integer quantity;

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