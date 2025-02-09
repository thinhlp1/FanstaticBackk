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
 * The persistent class for the status_type database table.
 * 
 */
@Table(name = "status_type")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusType  {
	
	@Id
	private String id;

	private String description;

	@Column(name = "status_type")
	private String statusType;

	// bi-directional many-to-one association to Status
	@OneToMany(mappedBy = "statusType")
	private List<Status> statuses;


}