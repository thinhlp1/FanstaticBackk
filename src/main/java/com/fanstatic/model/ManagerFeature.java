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
 * The persistent class for the manager_feature database table.
 * 
 */
@Table(name = "manager_feature")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerFeature  {
	
	@Id
	private String id;

	private String description;

	private String name;

	// // bi-directional many-to-one association to FeaturePermission
	// @OneToMany(mappedBy = "managerFeature")
	// private List<FeaturePermission> featurePermissions;

	// // bi-directional many-to-one association to RolePermission
	// @OneToMany(mappedBy = "managerFeature")
	// private List<RolePermission> rolePermissions;

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