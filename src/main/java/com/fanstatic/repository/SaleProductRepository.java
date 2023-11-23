package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.SaleProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SaleProductRepository extends JpaRepository<SaleProduct, Integer> {
      public Optional<List<SaleProduct>> findAllByActiveIsTrue();

    public Optional<List<SaleProduct>> findAllByActiveIsFalse();

    public Optional<SaleProduct> findByIdAndActiveIsTrue(int id);

    public Optional<SaleProduct> findByIdAndActiveIsFalse(int id);

      public Optional<List<SaleProduct>> findBySaleEventId(int id);
}
