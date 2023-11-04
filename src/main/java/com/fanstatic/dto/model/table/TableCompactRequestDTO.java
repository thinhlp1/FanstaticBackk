package com.fanstatic.dto.model.table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableCompactRequestDTO {

    private int id;

    private int numberTable;

    private int tableType;
}
