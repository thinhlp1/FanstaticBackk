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
 * The persistent class for the order_item_option database table.
 * 
 */

@Table(name = "order_item_option")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemOption  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// bi-directional many-to-one association to ExtraPortion
	@ManyToOne
	private Option option;

	// bi-directional many-to-one association to OrderItem
	@ManyToOne
	@JoinColumn(name = "order_item_id")
	private OrderItem orderItem;

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