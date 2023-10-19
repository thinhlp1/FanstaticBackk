package com.fanstatic.repository;

import com.fanstatic.model.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Integer> {
}
