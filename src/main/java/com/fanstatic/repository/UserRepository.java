package com.fanstatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fanstatic.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<List<User>> findAllByActiveIsTrue();

    public Optional<List<User>> findAllByActiveIsFalse();

    public Optional<User> findByIdAndActiveIsTrue(Integer id);

    public Optional<User> findByIdAndActiveIsFalse(Integer id);

    public Optional<User> findByNumberPhoneAndActiveIsTrue(String numberPhone);

    public Optional<User> findByNumberPhoneAndActiveIsTrueAndIdNot(String numberPhone, Integer id);

    public Optional<User> findByEmailAndActiveIsTrue(String email);

    public Optional<User> findByEmailAndActiveIsTrueAndIdNot(String email, Integer id);

    public Optional<User> findByCccdCmndAndActiveIsTrue(String cccdCmnd);

    public Optional<User> findByCccdCmndAndActiveIsTrueAndIdNot(String cccdCmnd, Integer id);

    @Query("SELECT COUNT(u) FROM User u WHERE u.employeeCode LIKE %:employeeCode%")
    public Optional<Integer> countByEmployeeCodeLike(@Param("employeeCode") String employeeCode);
}
