package com.fanstatic.repository;

import com.fanstatic.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SupplierRepository extends JpaRepository<Supplier, String> {
}
