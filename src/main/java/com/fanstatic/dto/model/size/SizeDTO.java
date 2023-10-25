package com.fanstatic.dto.model.size;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SizeDTO extends ResponseDataDTO{
    private int id;

	private byte active;

	private String code;

	private String name;
}
