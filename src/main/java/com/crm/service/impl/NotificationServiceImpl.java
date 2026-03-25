package com.crm.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.entity.Notification;
import com.crm.repo.NotificationRepository;
import com.crm.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void sendNotification(Notification notification) {
        // Integrate your SMS/Email API here
        String contact = notification.getCustomerContact() != null ? notification.getCustomerContact() : "Unknown";
        System.out.println("Sending notification to " + contact + ": " + notification.getMessage());

        // Mark as sent
        notification.setSent(true);
        notificationRepository.save(notification);
    }

    @Override
    public void sendBookingReminder(Long customerId, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMessage(message);
        notification.setScheduledAt(LocalDateTime.now());

        // Optional: fetch customer name & contact from DB
        // notification.setCustomerName("Rahul Mali");
        // notification.setCustomerContact("rahul@example.com");

        sendNotification(notification);
    }

    @Override
    public void sendMembershipExpiryAlert(Long customerId, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMessage(message);
        notification.setScheduledAt(LocalDateTime.now());

        // Optional: fetch customer name & contact from DB
        // notification.setCustomerName("Rahul Mali");
        // notification.setCustomerContact("rahul@example.com");

        sendNotification(notification);
    }

    @Override
    public List<Notification> getPendingNotifications() {
        return notificationRepository.findPendingNotifications(LocalDateTime.now());
    }
}