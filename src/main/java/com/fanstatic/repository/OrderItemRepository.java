package com.fanstatic.repository;

import com.fanstatic.model.Order;
import com.fanstatic.model.OrderItem;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
        public Optional<OrderItem> findByIdAndOrder(Integer id, Order order);

        @Query("SELECT SUM(oi.quantityCompleted) FROM OrderItem oi WHERE oi.order.createAt >= :startDate AND oi.order.createAt <= :endDate AND oi.status.id IN :orderStateId")
        Integer countSoldProductsByDateRangeAndState(Date startDate, Date endDate, List<String> orderStateId);

         // lay top 10 Product
        @Query("SELECT oi.product, SUM(oi.quantityCompleted) FROM OrderItem oi JOIN oi.order o WHERE oi.order.createAt >= :startDate AND oi.order.createAt <= :endDate AND oi.status.id IN :orderStateIds GROUP BY oi.product ORDER BY SUM(oi.quantityCompleted) DESC")
        List<Object[]> findTop10BestSellingProductsByRangeAndStates(Date startDate, Date endDate,
                        List<String> orderStateIds);

         // lay top 10 comboProduct              
         @Query("SELECT oi.comboProduct, SUM(oi.quantityCompleted) FROM OrderItem oi JOIN oi.order o WHERE oi.order.createAt >= :startDate AND oi.order.createAt <= :endDate AND oi.status.id IN :orderStateIds GROUP BY oi.comboProduct ORDER BY SUM(oi.quantityCompleted) DESC")
         List<Object[]> findTop10BestSellingComboProductsByRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);

         // lay top 10 productvariant
         @Query("SELECT oi.productVarient, SUM(oi.quantityCompleted) FROM OrderItem oi JOIN oi.order o WHERE oi.order.createAt >= :startDate AND oi.order.createAt <= :endDate AND oi.status.id IN :orderStateIds GROUP BY oi.productVarient ORDER BY SUM(oi.quantityCompleted) DESC")
         List<Object[]> findTop10BestSellingProductVariantByRangeAndStates(Date startDate, Date endDate, List<String> orderStateIds);
      
      
         public List<OrderItem> findAllByOrder(Order order);

}
