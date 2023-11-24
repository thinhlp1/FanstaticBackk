package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.User;
import com.google.common.base.Optional;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
        @Query("SELECT o FROM Order o " +
                        "WHERE o.status.id != 'COMPLETE' " +
                        "AND o.createAt >= :time " +
                        "ORDER BY o.createAt DESC") // Sắp xếp theo thời gian tạo giảm dần
        List<Order> findOrdersCreated(@Param("time") Date time);

        @Query("SELECT o FROM Order o " +
                        "WHERE o.status.id = 'AWAIT_CHECKOUT' " +
                        "AND o.createAt >= :time " +
                        "ORDER BY o.createAt DESC")
        List<Order> findOrdersCreatedAwaitCheckout(Date time);

        @Query("SELECT o FROM Order o " +
                        "WHERE (o.status.id = 'AWAIT_CHECKOUT' " +
                        "OR o.status.id = 'PROCESSING' " +
                        "OR o.status.id = 'CANCEL' " +
                        "OR o.status.id = 'CONFIRMING') " +
                        "AND o.customer.id = :customerId " +
                        "AND o.createAt >= :time")
        Optional<List<Order>> findOrderUser(@Param("customerId") Integer customerId, @Param("time") Date time);

        List<Order> findAllByCustomerId(Integer customerId);

        List<Order> findByRootOrder(Integer rootOrder);

        boolean existsById(Integer id);

}
