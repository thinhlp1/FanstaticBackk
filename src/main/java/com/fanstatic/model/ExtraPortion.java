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

/**
 * The persistent class for the extra_portion database table.
 * 
 */

@Table(name="extra_portion")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtraPortion  {
	
	@Id
	@Column(name="extra_portion_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int extraPortionId;

	private byte active;

	private String code;

	private String name;

	private Long price;

	private String type;

	//bi-directional many-to-one association to Category
	@ManyToOne
	private Category category;

	//bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name="image_id")
	private File image;

	//bi-directional many-to-one association to OrderExtraPortion
	@OneToMany(mappedBy="extraPortion")
	private List<OrderExtraPortion> orderExtraPortions;


	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_at")
	private Date updateAt;

	@OneToOne
	@JoinColumn(name="update_by")
	private User updateBy;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_at")
	private Date createAt;
	
	@OneToOne
	@JoinColumn(name="create_by")
	private User createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="delete_at")
	private Date deleteAt;

	@OneToOne
	@JoinColumn(name="delete_by")
	private User deleteBy;

}