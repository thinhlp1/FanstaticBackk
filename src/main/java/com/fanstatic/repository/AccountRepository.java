package com.fanstatic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fanstatic.model.Account;

import jakarta.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  public Optional<Account> findByNumberPhoneAndActiveIsTrue(String numberPhone);

  public Optional<Account> findByUserIdAndActiveIsTrue(Integer userId);

  public Optional<Account> findByUserId(Integer userId);

  @Modifying
  @Transactional
  @Query("UPDATE Account a SET a.active = :active WHERE a.role.id = :roleId")
  void updateActiveByRoleId(@Param("active") byte active, @Param("roleId") int roleId);


}
