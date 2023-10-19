package com.fanstatic.model;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**


/**
 * The persistent class for the flavor database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Flavor  {
	
	@Id
	private int id;

	private byte active;

	private String code;

	private String description;

	private String name;


	//bi-directional many-to-one association to Category
	@ManyToOne
	private Category category;

	//bi-directional many-to-one association to Unit
	@ManyToOne
	private Unit unit;

	//bi-directional many-to-one association to WarehouseDeliverItem
	@OneToMany(mappedBy="flavor")
	private List<WarehouseDeliverItem> warehouseDeliverItems;

	//bi-directional many-to-one association to WarehouseInventoryItem
	@OneToMany(mappedBy="flavor")
	private List<WarehouseInventoryItem> warehouseInventoryItems;

	//bi-directional many-to-one association to WarehouseReceiveItem
	@OneToMany(mappedBy="flavor")
	private List<WarehouseReceiveItem> warehouseReceiveItems;


	
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