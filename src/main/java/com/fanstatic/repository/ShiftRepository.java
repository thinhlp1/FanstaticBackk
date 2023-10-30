package com.fanstatic.repository;

import com.fanstatic.model.Shift;
import com.fanstatic.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ShiftRepository extends JpaRepository<Shift, String> {
    public Optional<List<Shift>> findAllByActiveIsTrue();

    public Optional<List<Shift>> findAllByActiveIsFalse();

    public Optional<Shift> findByIdAndActiveIsTrue(String id);

    public Optional<Shift> findByCodeAndActiveIsTrue(String code);

    public Optional<Shift> findByIdAndActiveIsFalse(String id);

    public Optional<Shift> findByShiftAndActiveIsTrue(String shift);

    public Optional<Shift> findByCodeAndActiveIsTrueAndIdNot(String code, String id);

    public Optional<Shift> findByShiftAndActiveIsTrueAndIdNot(String shift, String id);
}
