package com.fanstatic.dto.model.product;

import java.math.BigInteger;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fanstatic.dto.ResponseDataDTO;
import com.google.firebase.internal.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDTO extends ResponseDataDTO {
	private int id;

	@NotNull(message = "{NotNull.category.code}")
	@Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.category.code}")
	private String code;

	@NotBlank(message = "{NotBlank.category.name}")
	@Size(min = 1, max = 50, message = "{Size.category.name}")
	private String name;

	@NonNull
	private BigInteger price;

	private List<Integer> categoriesId;

	private List<ProductVarientRequestDTO> productVarients;

	private List<MultipartFile> imageFiles;

	private MultipartFile descriptionFile;

}
