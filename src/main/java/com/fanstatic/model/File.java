package com.fanstatic.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**

/**
 * The persistent class for the file database table.
 * 
 */

@Entity
@Table(name = "file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File  {
	
	@Id
	private int id;

	private byte active;

	private String extension;

	@Column(name="link_in_project")
	private String linkInProject;

	private String name;

	private int size;


}