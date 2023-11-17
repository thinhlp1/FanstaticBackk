package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.SaleProduct;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE sp.product.id = :productId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByProductId(@Param("productId") Integer productId);

    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE sp.productVarient.id = :productId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByProductVarientId(@Param("productId") Integer productVarientId);

    @Query("SELECT se FROM SaleEvent se JOIN SaleProduct sp ON se.id = sp.saleEvent.id " +
            "WHERE sp.comboProduct.id = :productId AND se.startAt <= CURRENT_TIMESTAMP AND se.endAt >= CURRENT_TIMESTAMP")
    Optional<SaleEvent> findSaleByComboId(@Param("productId") Integer comboId);
}
