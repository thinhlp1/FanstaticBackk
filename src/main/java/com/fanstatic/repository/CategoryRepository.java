package com.fanstatic.repository;

import com.fanstatic.model.Category;
import com.fanstatic.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Optional<List<Category>> findAllByActiveIsTrue();

    public Optional<List<Category>> findAllByActiveIsFalse();

    public Optional<List<Category>> findByLevel(Integer level);

    public Optional<Category> findByCodeAndActiveIsTrue(String code);

    public Optional<List<Category>> findByParentCategoryAndActiveIsTrue(Category category);

    public Optional<Category> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<Category> findByIdAndActiveIsTrue(Integer id);

    public Optional<Category> findByIdAndActiveIsFalse(Integer id);

    public Optional<List<Category>> findByLevelAndActiveIsTrue(Integer level);

    public Optional<List<Category>> findByLevelAndActiveIsFalse(Integer level);


}
