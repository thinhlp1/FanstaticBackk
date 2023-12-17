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
 * 
 * /**
 * The persistent class for the systemlog database table.
 * 
 */
@Entity
@Table(name = "systemlog")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Systemlog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "action")
	private Permission action;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "action_at")
	private Date actionAt;

	private String description;

	@Column(name = "object_id")
	private String objectId;

	@ManyToOne
	@JoinColumn(name = "type")
	private ManagerFeature type;

	@Column(name = "object_name")
	private String objectName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

}