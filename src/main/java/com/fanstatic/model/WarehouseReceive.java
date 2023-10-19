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
 * 
 * 
 * /**
 * The persistent class for the warehouse_receive database table.
 * 
 */

@Table(name = "warehouse_receive")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceive  {
	
	@Id
	private String id;

	private byte active;

	private String description;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File file;

	// bi-directional many-to-one association to Supplier
	@ManyToOne
	private Supplier supplier;

	// bi-directional many-to-one association to WarehouseReceiveItem
	@OneToMany(mappedBy = "warehouseReceive")
	private List<WarehouseReceiveItem> warehouseReceiveItems;

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