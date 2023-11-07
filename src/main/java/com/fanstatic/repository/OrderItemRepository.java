package com.fanstatic.repository;

import com.fanstatic.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
}
