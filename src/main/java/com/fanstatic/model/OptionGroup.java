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
@Table(name = "`option_group`")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private byte share;

    private byte multichoice;

    @OneToMany(mappedBy = "optionGroup")
    List<Option> options;

}