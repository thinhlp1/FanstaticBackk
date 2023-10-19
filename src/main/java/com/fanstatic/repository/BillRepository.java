package com.fanstatic.repository;

import com.fanstatic.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BillRepository extends JpaRepository<Bill, Integer> {
}
