package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.SaleProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE se.active = 1 AND sp.product.id = :productId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByProductId(@Param("productId") Integer productId);

    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE  se.active = 1 AND sp.productVarient.id = :productVarientId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByProductVarientId(@Param("productVarientId") Integer productVarientId);

    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE se.active = 1 AND sp.comboProduct.id = :comboId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByComboId(@Param("comboId") Integer comboId);
      public Optional<List<SaleProduct>> findAllByActiveIsTrue();

    public Optional<List<SaleProduct>> findAllByActiveIsFalse();

    public Optional<SaleProduct> findByIdAndActiveIsTrue(int id);

    public Optional<SaleProduct> findByIdAndActiveIsFalse(int id);
}
