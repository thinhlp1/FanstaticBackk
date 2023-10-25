package com.fanstatic.repository;

import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.model.Size;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductVarientRepository extends JpaRepository<ProductVarient, Integer> {

    @Query("SELECT COUNT(pv) FROM ProductVarient pv WHERE pv.product.id = :product_id")
    public int countByProductId(@Param("product_id") int product_id);

    @Query("SELECT pv FROM ProductVarient pv WHERE pv.product.id = :product_id AND pv.size.id = :size_id")
    Optional<ProductVarient> findByProductAndSize(@Param("product_id") int product_id, @Param("size_id") int size_id);

    public List<ProductVarient> findByProduct(Product product);

    public Optional<ProductVarient> findByIdAndActiveIsFalse(int id);

    public Optional<ProductVarient> findByIdAndActiveIsTrue(int id);

}
