package com.fanstatic.repository;

import com.fanstatic.model.Size;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SizeRepository extends JpaRepository<Size, Integer> {
    public Optional<List<Size>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<Size>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<Size> findByIdAndActiveIsTrue(int id);

    public Optional<Size> findByCodeAndActiveIsTrue(String code);

    public Optional<Size> findByIdAndActiveIsFalse(int id);

    public Optional<Size> findByNameAndActiveIsTrue(String name);

    public Optional<Size> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<Size> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);

}
