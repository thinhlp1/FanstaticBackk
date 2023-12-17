package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.User;
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
            "AND o.createAt >= :time " +
            "ORDER BY o.createAt DESC") // Sắp xếp theo thời gian tạo giảm dần
    List<Order> findOrdersCreated(@Param("time") Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE o.createAt >= :time " +
            "ORDER BY o.createAt DESC") // Sắp xếp theo thời gian tạo giảm dần
    List<Order> findOrderInDate(@Param("time") Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE o.status.id = 'CONFIRMING' " +
            "AND o.createAt >= :time " +
            "ORDER BY o.createAt DESC") // Sắp xếp theo thời gian tạo giảm dần
    List<Order> findOrderConfirming(@Param("time") Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE EXTRACT(YEAR FROM o.createAt) = :year " +
            "AND EXTRACT(MONTH FROM o.createAt) = :month " +
            "ORDER BY o.createAt DESC")
    List<Order> findOrdersInMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT o FROM Order o " +
            "WHERE EXTRACT(YEAR FROM o.createAt) = :year " +
            "ORDER BY o.createAt DESC")
    List<Order> findOrdersInYear(@Param("year") int year);

    @Query("SELECT o FROM Order o " +
            "WHERE o.status.id = 'AWAIT_CHECKOUT' " +
            "AND o.createAt >= :time " +
            "ORDER BY o.createAt DESC")
    List<Order> findOrdersCreatedAwaitCheckout(Date time);

    @Query("SELECT o FROM Order o " +
            "WHERE (o.status.id = 'AWAIT_CHECKOUT' " +
            "OR o.status.id = 'PROCESSING' " +
            "OR o.status.id = 'CONFIRMING') " +
            "AND o.customer.id = :customerId " +
            "AND o.createAt >= :time")
    Optional<List<Order>> findOrderUser(@Param("customerId") Integer customerId, @Param("time") Date time);

    List<Order> findAllByCustomerId(Integer customerId);

    @Query("SELECT COUNT(0) FROM Order o " +
            "WHERE o.status.id != 'COMPLETE' " +
            "AND o.voucher.id = :voucherId " +
            "AND o.id = :orderId") // Sắp xếp theo thời gian tạo giảm dần
    Integer findOrderCustomerIdAndVoucher(@Param("voucherId") Integer voucherId, @Param("orderId") Integer orderId);

    List<Order> findByRootOrder(Integer rootOrder);

    boolean existsById(Integer id);

    // đếm số lượng oder
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate AND o.status.id IN :orderStateIds")
    Long countOrdersByDateRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);

    // tính tổng tiền
    @Query("SELECT SUM( o.total ) FROM Order o WHERE o.createAt >= :startDate AND o.createAt <= :endDate AND o.status.id IN :orderStateIds")
    BigInteger calculateRevenueByDateRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);

}
