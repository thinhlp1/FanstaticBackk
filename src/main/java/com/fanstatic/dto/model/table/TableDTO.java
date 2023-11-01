package com.fanstatic.dto.model.table;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableDTO extends ResponseDataDTO {
    private int id;

	private byte active;

	private int capacity;

	private String name;

    private int numberTable;

    private TableTypeDTO tableTypeDTO;
}
