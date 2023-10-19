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
 * The persistent class for the feature_permission database table.
 * 
 */

@Table(name="feature_permission")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeaturePermission  {
	
	@Id
	private int id;

	private String description;

	//bi-directional many-to-one association to ManagerFeature
	@ManyToOne
	@JoinColumn(name="feature_id")
	private ManagerFeature managerFeature;

	//bi-directional many-to-one association to Permission
	@ManyToOne
	private Permission permission;

	
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