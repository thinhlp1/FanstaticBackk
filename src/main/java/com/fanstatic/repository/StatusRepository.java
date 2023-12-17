package com.fanstatic.repository;

import com.fanstatic.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StatusRepository extends JpaRepository<Status, String> {
}
