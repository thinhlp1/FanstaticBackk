package com.fanstatic.dto.model.table;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableRequestDTO extends ResponseDataDTO{
    private List<TableCompactRequestDTO> newTables;

    private List<Integer> deleteTables;

    private List<TableCompactRequestDTO> updateTables;

}
