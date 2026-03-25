package com.crm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.NotificationRequest;
import com.crm.entity.Notification;
import com.crm.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
//    @PostMapping("/booking-reminder")
//    public String bookingReminder(@RequestParam Long customerId, @RequestParam String message) {
//        notificationService.sendBookingReminder(customerId, message);
//        return "Booking reminder sent!";
//    }
//    
    
//    @PostMapping("/membership-expiry")
//    public String membershipExpiry(@RequestParam Long customerId, @RequestParam String message) {
//        notificationService.sendMembershipExpiryAlert(customerId, message);
//        return "Membership expiry alert sent!";
//    }

 // Booking Reminder
    @PostMapping("/booking-reminder")
    public String bookingReminder(@RequestBody NotificationRequest request) {
        notificationService.sendBookingReminder(request.getCustomerId(), request.getMessage());
        return "Booking reminder sent!";
    }

    // Membership Expiry Alert
    @PostMapping("/membership-expiry")
    public String membershipExpiry(@RequestBody NotificationRequest request) {
        notificationService.sendMembershipExpiryAlert(request.getCustomerId(), request.getMessage());
        return "Membership expiry alert sent!";
    }

    // Pending notifications
    @GetMapping("/pending")
    public List<Notification> pendingNotifications() {
        return notificationService.getPendingNotifications();
    }
   
}
