package com.fanstatic.repository;

import com.fanstatic.model.FeaturePermission;
import com.fanstatic.model.ManagerFeature;
import com.fanstatic.model.Permission;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FeaturePermissionRepository extends JpaRepository<FeaturePermission, Integer> {
   public List<FeaturePermission> findByManagerFeature(ManagerFeature managerFeature);
  public Optional<FeaturePermission> findByManagerFeatureAndPermission(ManagerFeature managerFeature,
                        Permission permission);
}
