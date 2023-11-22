package com.fanstatic.repository;

import com.fanstatic.model.Category;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductCategory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    @Modifying
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId")
    void deleteByProductId(@Param("productId") int productId);

    List<ProductCategory> findByProduct(Product product);

    @Query("SELECT pc.category FROM ProductCategory pc " +
            "WHERE pc.product.id = :productId AND pc.category.level = :categoryLevel")
    Category findCategoryByProductIdAndCategoryLevel(@Param("productId") int productId,
            @Param("categoryLevel") int categoryLevel);

    

}
