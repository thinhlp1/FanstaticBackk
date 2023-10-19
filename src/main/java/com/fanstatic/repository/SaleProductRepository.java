package com.fanstatic.repository;

import com.fanstatic.model.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
}
