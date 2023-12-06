package com.fanstatic.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fanstatic.model.Table;

public interface TableRepository extends JpaRepository<Table, Integer> {
  public Optional<List<Table>> findAllByActiveIsTrue();

  public Optional<List<Table>> findAllByActiveIsFalse();

  public Optional<Table> findByIdAndActiveIsTrue(int id);

  public Optional<Table> findByIdAndActiveTrue(int id);

  public Optional<Table> findByNumberTableAndActiveIsTrue(int numberTable);

  public Optional<Table> findByNumberTable(int numberTable);

  public Optional<Table> findByIdAndActiveIsFalse(int id);

  @Query("SELECT t FROM Table t WHERE t.id NOT IN " +
      "(SELECT ot.table.id FROM OrderTable ot " +
      "WHERE ot.order.status.id <> 'COMPLETE' " +
      "AND ot.order.createAt >= :twentyFourHoursAgo)")
  List<Table> findTablesWithoutOrdersInLast24Hours(Date twentyFourHoursAgo);

}
