package com.fanstatic.repository;

import com.fanstatic.model.ComboProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComboProductRepository extends JpaRepository<ComboProduct, Integer> {
}
