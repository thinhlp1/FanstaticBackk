package com.fanstatic.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fanstatic.model.RequestStaffNotification;

public interface RequestStaffNotificationRepository extends JpaRepository<RequestStaffNotification, Integer> {
    @Query("SELECT r FROM RequestStaffNotification r WHERE r.createAt >= :time AND r.employeeConfirm IS NULL")
    List<RequestStaffNotification> findUnconfirmedNotificationsIn24Hours(@Param("time") Date time);

    @Query("SELECT r FROM RequestStaffNotification r WHERE r.createAt >= :time AND r.employeeConfirm IS NOT NULL")
    List<RequestStaffNotification> findConfirmedNotificationsIn24Hours(@Param("time") Date time);

    @Query("SELECT r FROM RequestStaffNotification r WHERE r.createAt >= :time AND r.employeeConfirm IS NOT NULL")
    List<RequestStaffNotification> find(@Param("time") Date time);

}
