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
 * /**
 * The persistent class for the table database table.
 * 
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Table  {
	
	@Id
	private int id;

	private byte active;

	private int capacity;

	@Lob
	private String description;

	private String name;

	@Column(name = "number_table")
	private int numberTable;

	@Column(name = "table_type")
	private String tableType;

	// bi-directional many-to-one association to OrderTable
	@OneToMany(mappedBy = "table")
	private List<OrderTable> orderTables;

	// bi-directional many-to-one association to File
	@ManyToOne
	@JoinColumn(name = "image_id")
	private File image;

	// bi-directional many-to-one association to QrCode
	@ManyToOne
	@JoinColumn(name = "qr_code")
	private QrCode qrCode;

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