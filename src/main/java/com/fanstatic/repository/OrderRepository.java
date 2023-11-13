package com.fanstatic.repository;

import com.fanstatic.model.Order;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o " +
            "WHERE o.status.status != 'COMPLETE' " +
            "AND o.createAt >= :time")
    List<Order> findOrdersCreated(Date time);

    List<Order> findByRootOrder(Integer rootOrder);

    
}
