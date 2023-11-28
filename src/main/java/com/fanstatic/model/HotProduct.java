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
 * The persistent class for the hot_product database table.
 * 
 */

@Table(name = "hot_product")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotProduct  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_at")
	private Date endAt;

	private int serial;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_at")
	private Date startAt;

	// bi-directional many-to-one association to ComboProduct
	@ManyToOne
	@JoinColumn(name = "combo_id")
	private ComboProduct comboProduct;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

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