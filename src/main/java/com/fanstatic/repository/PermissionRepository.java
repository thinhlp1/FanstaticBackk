package com.fanstatic.repository;

import com.fanstatic.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, String> {
}
