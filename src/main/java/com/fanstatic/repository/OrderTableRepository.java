package com.fanstatic.repository;

import com.fanstatic.model.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
}
