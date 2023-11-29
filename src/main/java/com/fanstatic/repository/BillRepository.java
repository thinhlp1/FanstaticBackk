package com.fanstatic.repository;

import com.fanstatic.model.Bill;
import com.google.common.base.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Integer> {
        public Optional<Bill> findByOrderOrderId(int id);

        @Query("SELECT b FROM Bill b " +
                        "WHERE (b.status.id = 'AWAIT_PAYMENT' OR  b.status.id = 'PAID' ) AND b.order.orderId = :orderId")
        Optional<Bill> findBillCheckouted(@Param("orderId") int orderId);

        @Query("SELECT b FROM Bill b " +
                        "WHERE b.status.id = :status AND b.order.orderId = :orderId")
        Optional<Bill> findBillByOrderIdAndStatus(@Param("orderId") int orderId, @Param("status") String status);

        @Query("SELECT b FROM Bill b " +
                        "WHERE b.status.id = :status AND b.order.orderId = :orderId")
        Optional<Bill> findBillAwaiPayment(@Param("orderId") int orderId, @Param("status") String status);

}
