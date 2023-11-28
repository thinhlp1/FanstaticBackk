package com.fanstatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.OptionGroup;

public interface OptionGroupRepository extends JpaRepository<OptionGroup, Integer> {
    public Optional<List<OptionGroup>> findAllByShareIsTrue();

}
