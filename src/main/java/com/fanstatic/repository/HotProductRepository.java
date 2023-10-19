package com.fanstatic.repository;

import com.fanstatic.model.HotProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotProductRepository extends JpaRepository<HotProduct, Integer> {
}
