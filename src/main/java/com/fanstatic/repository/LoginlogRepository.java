package com.fanstatic.repository;

import com.fanstatic.model.Loginlog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginlogRepository extends JpaRepository<Loginlog, Integer> {
}
