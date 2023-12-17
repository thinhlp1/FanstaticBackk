package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.OrderItem;
import com.fanstatic.model.OrderSurcharge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSurchargeRepository extends JpaRepository<OrderSurcharge, Integer> {
    public List<OrderSurcharge> findAllByOrder(Order order);

}
