package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDataDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDTO extends ResponseDataDTO{
	private int id;

	private String code;

	private String name;

	private BigInteger price;

    private List<Integer> categoriesId;

    private List<ProductVarientRequestDTO> productVarients;

	private List<MultipartFile> imageFiles;

	private MultipartFile descriptionFile;

}
