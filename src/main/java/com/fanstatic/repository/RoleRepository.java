package com.fanstatic.repository;

import com.fanstatic.model.Role;
import com.fanstatic.model.User;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Optional<List<Role>> findAllByActiveIsTrueOrderByCreateAtDesc();

    public Optional<List<Role>> findAllByActiveIsFalseOrderByCreateAtDesc();

    public Optional<Role> findByIdAndActiveIsTrue(int id);

    public Optional<Role> findByCodeAndActiveIsTrue(String code);

    public Optional<Role> findByIdAndActiveIsFalse(int id);

    public Optional<Role> findByNameAndActiveIsTrue(String name);

    public Optional<Role> findByCodeAndActiveIsTrueAndIdNot(String code, Integer id);

    public Optional<Role> findByNameAndActiveIsTrueAndIdNot(String code, Integer id);

    @Query("SELECT COUNT(r) FROM Role r WHERE r.code = :code")
    public int countByCode(@Param("code") String code);

}
