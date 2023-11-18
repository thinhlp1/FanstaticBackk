package com.fanstatic.repository;

import com.fanstatic.model.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FlavorRepository extends JpaRepository<Flavor, Integer> {
    public Optional<List<Flavor>> findAllByActiveIsTrue();

    public Optional<List<Flavor>> findAllByActiveIsFalse();

    public Optional<Flavor> findByIdAndActiveIsTrue(int id);

    public Optional<Flavor> findByIdAndActiveIsFalse(int id);

    public Optional<Flavor> findByCodeAndActiveIsTrue(String code);

    public Optional<Flavor> findByCodeAndActiveIsFalse(String code);

    public Optional<Flavor> findByNameAndActiveIsTrue(String name);

    public Optional<Flavor> findByNameAndActiveIsFalse(String name);
}
