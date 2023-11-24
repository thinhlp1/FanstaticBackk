package com.fanstatic.repository;

import com.fanstatic.model.SaleEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SaleEventRepository extends JpaRepository<SaleEvent, Integer> {
    public Optional<List<SaleEvent>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<SaleEvent>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<SaleEvent> findByIdAndActiveIsTrue(int id);

    public Optional<SaleEvent> findByIdAndActiveIsFalse(int id);

}
