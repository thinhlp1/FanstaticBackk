package com.fanstatic.repository;

import com.fanstatic.model.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
}
