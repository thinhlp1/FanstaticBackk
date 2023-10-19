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
 * The persistent class for the role_permission database table.
 * 
 */

@Table(name = "role_permission")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class 	RolePermission  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	// bi-directional many-to-one association to ManagerFeature
	@ManyToOne
	@JoinColumn(name="feature_permission_id")
	private FeaturePermission featurePermission;

	// bi-directional many-to-one association to Role
	@ManyToOne
	private Role role;

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