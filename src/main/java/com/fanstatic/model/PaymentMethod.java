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
 * The persistent class for the payment_method database table.
 * 
 */
@Table(name = "payment_method")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod  {
	
	@Id
	private String id;

	private String name;

	// bi-directional many-to-one association to Bill
	@OneToMany(mappedBy = "paymentMethod")
	private List<Bill> bills;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;

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