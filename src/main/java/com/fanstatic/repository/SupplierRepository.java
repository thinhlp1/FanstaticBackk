package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import com.fanstatic.model.Size;
import com.fanstatic.model.Supplier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
       public Optional<List<Supplier>> findAllByActiveIsTrue();

    public Optional<List<Supplier>> findAllByActiveIsFalse();

    public Optional<Supplier> findByIdAndActiveIsTrue(int id);

    public Optional<Supplier> findByIdAndActiveIsFalse(int id);

    public Optional<Supplier> findByNameAndActiveIsTrue(String name);

       public Optional<Supplier> findByNameAndActiveIsTrueAndIdNot(String name, int id);
}
