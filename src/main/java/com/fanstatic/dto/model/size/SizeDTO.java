package com.fanstatic.dto.model.size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeDTO {
    private int id;

	private byte active;

	private String code;

	private String name;
}
