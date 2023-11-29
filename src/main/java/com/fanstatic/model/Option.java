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
 * The persistent class for the flavor database table.
 * 
 */
@Entity
@Table(name = "`option`")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private Long price;

	@ManyToOne
	@JoinColumn(name = "option_group_id")
	private OptionGroup optionGroup;

	private byte active;

}