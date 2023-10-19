package com.fanstatic.model;


import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 
 * 
 * /**
 * The persistent class for the status database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Status  {
	
	@Id
	private String id;

	private String description;

	private String status;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "status")
	private List<Order> orders;

	// bi-directional many-to-one association to OrderItem
	@OneToMany(mappedBy = "status")
	private List<OrderItem> orderItems;

	// bi-directional many-to-one association to StatusType
	@ManyToOne
	@JoinColumn(name = "status_type_id")
	private StatusType statusType;

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