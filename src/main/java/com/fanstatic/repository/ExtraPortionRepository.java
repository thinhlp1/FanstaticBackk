package com.fanstatic.repository;

import com.fanstatic.model.ExtraPortion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ExtraPortionRepository extends JpaRepository<ExtraPortion, Integer> {
    public Optional<List<ExtraPortion>> findAllByActiveIsTrue();

    public Optional<List<ExtraPortion>> findAllByActiveIsFalse();

    public Optional<ExtraPortion> findByExtraPortionIdAndActiveIsTrue(int id);

    public Optional<ExtraPortion> findByExtraPortionIdAndActiveIsFalse(int id);

    public Optional<ExtraPortion> findByCodeAndActiveIsTrue(String code);

    public Optional<ExtraPortion> findByCodeAndActiveIsFalse(String code);

    public Optional<ExtraPortion> findByNameAndActiveIsTrue(String name);

    public Optional<ExtraPortion> findByNameAndActiveIsFalse(String name);

}
