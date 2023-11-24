package com.fanstatic.repository;

import com.fanstatic.model.FlavorCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlavorCategoryRepository extends JpaRepository<FlavorCategory, Integer> {
    public Optional<List<FlavorCategory>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<FlavorCategory>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<FlavorCategory> findByIdAndActiveIsTrue(int id);

    public Optional<FlavorCategory> findByIdAndActiveIsFalse(int id);

    public Optional<FlavorCategory> findByCodeAndActiveIsTrue(String code);

    public Optional<FlavorCategory> findByCodeAndActiveIsFalse(String code);

    public Optional<FlavorCategory> findByNameAndActiveIsTrue(String name);

    public Optional<FlavorCategory> findByNameAndActiveIsFalse(String name);

    public List<FlavorCategory> findAllByOrderByCreateAtDesc();

}
