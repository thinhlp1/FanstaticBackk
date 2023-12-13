package com.fanstatic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.fanstatic.model.Notification;
import com.fanstatic.model.User;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByReceiverOrderBySendAtDesc(User receiver);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.seenAt IS NOT NULL AND n.seenAt < :cutoffDate")
    void deleteBySeenAtBefore(Date cutoffDate);
}
