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

	@NotNull(message = "{NotNull.product.code}")
	@Pattern(regexp = "^[A-Z0-9]*$", message = "{Pattern.product.code}")
	private String code;

	@NotBlank(message = "{NotBlank.product.name}")
	@Size(min = 1, max = 50, message = "{Size.product.name}")
	private String name;

	@NotNull(message = "{NotNull.product.price}")
	private BigInteger price;

	private List<Integer> categoriesId;

	private List<ProductVarientRequestDTO> productVarients;

	private List<MultipartFile> imageFiles;

	private MultipartFile descriptionFile;

}
