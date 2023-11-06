package com.fanstatic.repository;

import com.fanstatic.model.Product;
import com.fanstatic.model.ProductImage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    void deleteByImageId(Integer id);

    public List<ProductImage> findByProduct(Product product);

}
