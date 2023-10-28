package com.fanstatic.dto.model.category;

import java.util.List;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO extends ResponseDataDTO {
    private int id;

    private byte active;

    private String code;

    private int level;

    private String name;

    private String imageUrl;
    // bi-directional many-to-one association to Category

    private List<ResponseDataDTO> childCategories;
}
