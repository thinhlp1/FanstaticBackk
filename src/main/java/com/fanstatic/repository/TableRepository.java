package com.fanstatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.Table;

public interface TableRepository extends JpaRepository<Table, Integer> {
  public Optional<List<Table>> findAllByActiveIsTrueOrderByCreateAtDesc();

  public Optional<List<Table>> findAllByActiveIsFalseOrderByCreateAtDesc();

  public Optional<Table> findByIdAndActiveIsTrue(int id);

  public Optional<Table> findByIdAndActiveTrue(int id);

  public Optional<Table> findByNumberTableAndActiveIsTrue(int numberTable);

  public Optional<Table> findByNumberTable(int numberTable);

  public Optional<Table> findByIdAndActiveIsFalse(int id);

}
