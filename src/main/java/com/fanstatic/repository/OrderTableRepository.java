package com.fanstatic.repository;

import com.fanstatic.model.OrderTable;
import org.springframework.data.repository.query.Param;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
        @Query("SELECT COUNT(o) FROM OrderTable o " +
                        "WHERE o.table.id = :tableId " +
                        "AND o.order.status.id <> 'COMPLETED' " +
                        "AND o.order.orderId <> :rootOrderId " +
                        "AND o.order.createAt >= :time")
        public int checkTalbeOccupiedAndRootId(@Param("tableId") int tableId, @Param("rootOrderId") int rootOrderId,
                        Date time);

        @Query("SELECT COUNT(o) FROM OrderTable o " +
                        "WHERE o.table.id = :tableId " +
                        "AND o.order.status.id <> 'COMPLETED' " +
                        "AND o.order.createAt >= :time")
        public int checkTalbeOccupied(@Param("tableId") int tableId, Date time);

      
}
