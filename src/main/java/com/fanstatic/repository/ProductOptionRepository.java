package com.fanstatic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fanstatic.model.Product;
import com.fanstatic.model.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Integer> {

    public List<ProductOption> findByProduct(Product product);

}
