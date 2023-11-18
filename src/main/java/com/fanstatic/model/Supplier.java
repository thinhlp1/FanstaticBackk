package com.fanstatic.model;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;


/**
 * 
 * 
 * /**
 * The persistent class for the supplier database table.
 * 
 */
@Entity
@Data
@Table(name = "supplier")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private byte active;

	private String address;

	private String name;

	@Column(name = "number_phone")
	private String numberPhone;

	// bi-directional many-to-one association to WarehouseReceive
	@OneToMany(mappedBy = "supplier")
	private List<WarehouseReceive> warehouseReceives;

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