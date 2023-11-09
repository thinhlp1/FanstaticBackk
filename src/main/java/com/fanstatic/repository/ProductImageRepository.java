package com.fanstatic.repository;

import com.fanstatic.model.Product;
import com.fanstatic.model.ProductImage;
import com.fanstatic.model.ProductVarient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    void deleteByImageId(Integer id);

    public List<ProductImage> findByProduct(Product product);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.productVariant = :productVariant")
    List<ProductImage> findByProductVarient(@Param("productVariant") ProductVarient productVariant);

}
