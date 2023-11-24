package com.fanstatic.repository;

import com.fanstatic.model.ComboProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComboProductRepository extends JpaRepository<ComboProduct, Integer> {

    public Optional<List<ComboProduct>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<ComboProduct>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<ComboProduct> findByIdAndActiveIsFalse(int id);

    public Optional<ComboProduct> findByIdAndActiveIsTrue(int id);

    public Optional<ComboProduct> findByNameAndActiveIsTrue(String name);

    public Optional<ComboProduct> findByNameAndActiveIsFalse(String name);

    public Optional<ComboProduct> findByCodeAndActiveIsTrue(String code);

    public Optional<ComboProduct> findByCodeAndActiveIsFalse(String code);

    public Optional<ComboProduct> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<ComboProduct> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public List<ComboProduct> findAllByOrderByCreateAtDesc();

    @Query("SELECT COALESCE(SUM(oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.status.id = 'COMPLETE' " +
            "AND oi.comboProduct.id = :comboId")
    Integer countSoldQuantityByProductId(@Param("comboId") Integer comboId);

}
