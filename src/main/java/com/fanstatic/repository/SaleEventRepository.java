package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SaleEventRepository extends JpaRepository<SaleEvent, Integer> {
}
