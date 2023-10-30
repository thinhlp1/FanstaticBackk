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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private byte active;

	private String extension;

	@Column(name="link_in_project")
	private String link;

	private String name;

	private long size;


	public File( byte active, String extension, String link, String name, long size) {
		this.active = active;
		this.extension = extension;
		this.link = link;
		this.name = name;
		this.size = size;
	}



}