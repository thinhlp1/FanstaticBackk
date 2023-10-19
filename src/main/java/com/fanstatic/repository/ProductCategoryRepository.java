package com.fanstatic.repository;

import com.fanstatic.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}
