package com.fanstatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.Table;

public interface TableRepository extends JpaRepository<Table, Integer> {
    public Optional<List<Table>> findAllByActiveIsTrue();

    public Optional<List<Table>> findAllByActiveIsFalse();

    public Optional<Table> findByIdAndActiveIsTrue(int id);

    public List<Table> findByIdInAndActiveTrue(List<Integer> ids);

    public Optional<Table> findByNumberTableAndActiveIsTrue(int numberTable);

      public Optional<Table> findByNumberTable(int numberTable);

    public Optional<Table> findByIdAndActiveIsFalse(int id);

}
