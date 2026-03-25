package com.crm.scheduler;


import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crm.entity.CustomerMembership;
import com.crm.repo.CustomerMembershipRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MembershipScheduler {

    private final CustomerMembershipRepository membershipRepo;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void expireMemberships() {

        List<CustomerMembership> memberships = membershipRepo.findAll();

        LocalDate today = LocalDate.now();

        memberships.forEach(m -> {
            if (m.isActive() && m.getEndDate() != null && m.getEndDate().isBefore(today)) {
                m.setActive(false);
                membershipRepo.save(m);
            }
        });

        System.out.println("✅ Membership expiry check completed");
    }
}