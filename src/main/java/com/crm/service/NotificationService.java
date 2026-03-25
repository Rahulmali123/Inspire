package com.crm.service;

import java.util.List;

import com.crm.entity.Notification;

public interface NotificationService {

    void sendNotification(Notification notification);

    void sendBookingReminder(Long customerId, String message);

    void sendMembershipExpiryAlert(Long customerId, String message);

    List<Notification> getPendingNotifications();
}