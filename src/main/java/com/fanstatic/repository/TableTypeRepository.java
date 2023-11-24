package com.fanstatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.TableType;

public interface TableTypeRepository extends JpaRepository<TableType, Integer> {
    public Optional<List<TableType>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<TableType>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<TableType> findByIdAndActiveIsTrue(int id);

    public Optional<TableType> findByCodeAndActiveIsTrue(String code);

    public Optional<TableType> findByIdAndActiveIsFalse(int id);

    public Optional<TableType> findByNameAndActiveIsTrue(String name);

    public Optional<TableType> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<TableType> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);

}
