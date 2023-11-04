package com.fanstatic.repository;

import com.fanstatic.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, String> {
    public Optional<List<Unit>> findAllByActiveIsTrue();

    public Optional<List<Unit>> findAllByActiveIsFalse();

    public Optional<Unit> findByIdAndActiveIsTrue(String id);

    public Optional<Unit> findByNameAndActiveIsTrue(String name);

    public Optional<Unit> findByIdAndActiveIsFalse(String id);

    public Optional<Unit> findBySignAndActiveIsTrue(String sign);

    public Optional<Unit> findByNameAndActiveIsTrueAndIdNot(String name, String id);

    public Optional<Unit> findBySignAndActiveIsTrueAndIdNot(String sign, String id);

}
