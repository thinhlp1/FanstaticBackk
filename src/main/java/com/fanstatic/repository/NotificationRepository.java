package com.fanstatic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
