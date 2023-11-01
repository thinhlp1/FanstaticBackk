package com.fanstatic.repository;

import com.fanstatic.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShiftRepository extends JpaRepository<Shift, Integer> {
}
