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
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name = "status")
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
	
	// bi-directional many-to-one association to StatusType
	@ManyToOne
	@JoinColumn(name = "status_type_id")
	private StatusType statusType;

	
}