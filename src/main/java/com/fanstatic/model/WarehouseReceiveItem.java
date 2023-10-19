package com.fanstatic.model;


import java.util.Date;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**

/**
 * The persistent class for the warehouse_receive_item database table.
 * 
 */

@Table(name="warehouse_receive_item")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiveItem  {
	
	@Id
	private int id;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expired_at")
	private Date expiredAt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date price;

	private int quantity;

	//bi-directional many-to-one association to Flavor
	@ManyToOne
	private Flavor flavor;

	//bi-directional many-to-one association to WarehouseReceive
	@ManyToOne
	@JoinColumn(name="receive_id")
	private WarehouseReceive warehouseReceive;

	
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