package com.fanstatic.repository;

import com.fanstatic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<User, Integer> {

    public Optional<List<User>> findAllByActiveIsTrue();

    public Optional<List<User>> findAllByRoleIdAndActiveIsTrue(Integer id);
    
    public Optional<List<User>> findAllByRoleId(Integer id);

    public Optional<List<User>> findAllByActiveIsFalse();

    public Optional<User> findByIdAndActiveIsTrue(Integer id);

    public Optional<User> findByIdAndActiveIsFalse(Integer id);

    public Optional<User> findByNumberPhoneAndActiveIsTrue(String numberPhone);

    public Optional<User> findByNumberPhoneAndActiveIsTrueAndIdNot(String numberPhone, Integer id);

    public Optional<User> findByEmailAndActiveIsTrue(String email);

    public Optional<User> findByEmailAndActiveIsTrueAndIdNot(String email, Integer id);


    @Query("SELECT COUNT(u) FROM User u WHERE u.employeeCode LIKE %:employeeCode%")
    public Optional<Integer> countByEmployeeCodeLike(@Param("employeeCode") String employeeCode);
}
