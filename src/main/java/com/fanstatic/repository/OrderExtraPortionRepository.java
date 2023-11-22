package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.OrderExtraPortion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderExtraPortionRepository extends JpaRepository<OrderExtraPortion, Integer> {
    public Optional<OrderExtraPortion> findByIdAndOrder(Integer id, Order order);
}
