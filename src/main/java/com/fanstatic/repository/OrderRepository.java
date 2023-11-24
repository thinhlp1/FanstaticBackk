package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.google.common.base.Optional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o " +
            "WHERE o.status.id != 'COMPLETE' " +
            "AND o.createAt >= :time")
    List<Order> findOrdersCreated(Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE o.status.id = 'AWAIT_CHECKOUT' " +
            "AND o.createAt >= :time")
    List<Order> findOrdersCreatedAwaitCheckout(Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE (o.status.id = 'AWAIT_CHECKOUT' " +
            "OR o.status.id = 'PROCESSING' " +
            "OR o.status.id = 'CANCEL' " +
            "OR o.status.id = 'CONFIRMING') " +
            "AND o.customer.id = :customerId " +
            "AND o.createAt >= :time")
    Optional<List<Order>> findOrderUser(@Param("customerId") Integer customerId, @Param("time") Date time);

    List<Order> findByRootOrder(Integer rootOrder);

    boolean existsById(Integer id);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate AND o.status.id IN :orderStateIds")
    Long countOrdersByDateRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);

    @Query("SELECT SUM( o.total ) FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate AND o.status.id IN :orderStateIds")
    BigInteger calculateRevenueByDateRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);

}
