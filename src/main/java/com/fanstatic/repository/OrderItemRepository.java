package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.OrderItem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
        public Optional<OrderItem> findByIdAndOrder(Integer id, Order order);

        public List<OrderItem> findAllByOrder(Order order);

}
