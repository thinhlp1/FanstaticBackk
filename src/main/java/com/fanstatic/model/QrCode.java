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
 * The persistent class for the qr_code database table.
 * 
 */

@Table(name = "qr_code")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QrCode  {
	
	@Id
	private int id;

	private byte active;

	private String content;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_at")
	private Date endAt;

	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_at")
	private Date startAt;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;

	// bi-directional many-to-one association to Table
	@OneToMany(mappedBy = "qrCode")
	private List<com.fanstatic.model.Table> tables;

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