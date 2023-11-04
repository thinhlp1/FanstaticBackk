package com.fanstatic.dto.model.table;

import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableTypeDTO extends ResponseDataDTO {

    private int id;

    private Integer capacity;

    private String name;

    private String code;

    private String imageUrl;
}
