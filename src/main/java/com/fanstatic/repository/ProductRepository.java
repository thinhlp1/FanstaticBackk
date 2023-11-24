package com.fanstatic.repository;

import com.fanstatic.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Optional<Product> findByCodeAndActiveIsTrue(String code);

    public Optional<List<Product>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<Product>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public List<Product> findAllByOrderByCreateAtDesc();

    public Optional<Product> findByIdAndActiveIsFalse(int id);

    public Optional<Product> findByIdAndActiveIsTrue(int id);

    public Optional<Product> findByNameAndActiveIsTrue(String name);

    public Optional<Product> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<Product> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);

    @Query("SELECT COUNT(r) FROM Product r WHERE r.code = :code")
    public int countByCode(@Param("code") String code);

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.status.id = 'COMPLETE' " +
            "AND oi.product.id = :productId")
    Integer countSoldQuantityByProductId(@Param("productId") Integer productId);

}
