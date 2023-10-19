package com.fanstatic.repository;

import com.fanstatic.model.RolePermission;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
        @Query("SELECT COUNT(rp) FROM RolePermission rp " +
                        "WHERE rp.role.id = :roleId " +
                        "AND rp.featurePermission.managerFeature.id = :manageFeatureId " +
                        "AND rp.featurePermission.permission.id = :permissionId")
        public long countByRoleAndManageFeatureAndPermission(
                        @Param("roleId") int roleId,
                        @Param("manageFeatureId") String manageFeatureId,
                        @Param("permissionId") String permissionId);

        @Modifying
        @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
        void deleteByRoleId(@Param("roleId") int roleId);

}
