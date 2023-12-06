package com.fanstatic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanstatic.model.Notification;
import com.fanstatic.model.User;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByReceiverOrderBySendAtDesc(User receiver);

}
