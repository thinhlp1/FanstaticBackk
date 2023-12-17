package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SaleEventRepository extends JpaRepository<SaleEvent, Integer> {
    public Optional<List<SaleEvent>> findAllByActiveIsTrue();

    public Optional<List<SaleEvent>> findAllByActiveIsFalse();

    //public Optional<List<SaleEvent>> findAllBySaleEventId(int id);

    public Optional<SaleEvent> findByIdAndActiveIsTrue(int id);

     public Optional<SaleEvent> findById(int id);

    public Optional<SaleEvent> findByIdAndActiveIsFalse(int id);

}
