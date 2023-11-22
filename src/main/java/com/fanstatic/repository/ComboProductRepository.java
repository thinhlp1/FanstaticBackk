package com.fanstatic.repository;

import com.fanstatic.model.ComboProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboProductRepository extends JpaRepository<ComboProduct, Integer> {

    public Optional<List<ComboProduct>> findAllByActiveIsTrue();

    public Optional<List<ComboProduct>> findAllByActiveIsFalse();

    public Optional<ComboProduct> findByIdAndActiveIsFalse(int id);

    public Optional<ComboProduct> findByIdAndActiveIsTrue(int id);

    public Optional<ComboProduct> findByNameAndActiveIsTrue(String name);


    public Optional<ComboProduct> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);
}
