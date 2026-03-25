package com.crm.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crm.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.sent = false AND n.scheduledAt <= :now")
    List<Notification> findPendingNotifications(LocalDateTime now);
}