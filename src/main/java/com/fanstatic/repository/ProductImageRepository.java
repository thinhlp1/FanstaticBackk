package com.fanstatic.repository;

import com.fanstatic.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    void deleteByImageId(Integer id);
}
