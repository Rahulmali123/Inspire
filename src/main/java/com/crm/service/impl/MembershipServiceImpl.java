package com.crm.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crm.dto.CustomerMembershipDto;
import com.crm.dto.MembershipPlanDto;
import com.crm.entity.CustomerMembership;
import com.crm.entity.MembershipPlan;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repo.CustomerMembershipRepository;
import com.crm.repo.MembershipPlanRepository;
import com.crm.service.MembershipService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipPlanRepository planRepo;
    private final CustomerMembershipRepository membershipRepo;

    // ==============================
    // 🔹 CREATE PLAN
    // ==============================
    @Override
    public MembershipPlanDto createPlan(MembershipPlanDto dto) {

        MembershipPlan plan = new MembershipPlan();
        plan.setName(dto.getName());
        plan.setDurationDays(dto.getDurationDays());
        plan.setPrice(dto.getPrice());
        plan.setDescription(dto.getDescription());

        planRepo.save(plan);

        return mapPlanToDto(plan);
    }

    // ==============================
    // 🔹 GET ALL PLANS
    // ==============================
    @Override
    public List<MembershipPlanDto> getAllPlans() {
        return planRepo.findAll()
                .stream()
                .map(this::mapPlanToDto)
                .collect(Collectors.toList());
    }

    // ==============================
    // 🔹 ASSIGN MEMBERSHIP
    // ==============================
    @Override
    public CustomerMembershipDto assignMembership(CustomerMembershipDto dto) {

        if (dto.getCustomerId() == null || dto.getPlanId() == null) {
            throw new IllegalArgumentException("CustomerId and PlanId are required");
        }

        MembershipPlan plan = planRepo.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + dto.getPlanId()));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(plan.getDurationDays());

        // ✅ NULL SAFE (IMPORTANT FIX)
        boolean isActive = dto.getActive() != null ? dto.getActive() : true;

        CustomerMembership membership = new CustomerMembership();
        membership.setCustomerId(dto.getCustomerId());
        membership.setPlan(plan);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setActive(isActive);

        membershipRepo.save(membership);

        return mapToDto(membership);
    }

    // ==============================
    // 🔹 RENEW MEMBERSHIP
    // ==============================
    @Override
    public CustomerMembershipDto renewMembership(Long id) {

        CustomerMembership membership = membershipRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + id));

        LocalDate currentEnd = membership.getEndDate() != null
                ? membership.getEndDate()
                : LocalDate.now();

        LocalDate newEndDate = currentEnd.plusDays(membership.getPlan().getDurationDays());

        membership.setEndDate(newEndDate);
        membership.setActive(true);

        membershipRepo.save(membership);

        return mapToDto(membership);
    }

    // ==============================
    // 🔹 UPGRADE MEMBERSHIP
    // ==============================
    @Override
    public CustomerMembershipDto upgradeMembership(Long id, Long newPlanId) {

        CustomerMembership membership = membershipRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership not found with id: " + id));

        MembershipPlan newPlan = planRepo.findById(newPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("New plan not found with id: " + newPlanId));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(newPlan.getDurationDays());

        membership.setPlan(newPlan);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setActive(true);

        membershipRepo.save(membership);

        return mapToDto(membership);
    }

    // ==============================
    // 🔹 GET MEMBERSHIP BY CUSTOMER
    // ==============================
    @Override
    public List<CustomerMembershipDto> getMembershipsByCustomer(Long customerId) {

        return membershipRepo.findByCustomerId(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ==============================
    // 🔹 PRIVATE MAPPERS
    // ==============================
    private CustomerMembershipDto mapToDto(CustomerMembership m) {
        return new CustomerMembershipDto(
                m.getId(),
                m.getCustomerId(),
                m.getPlan().getId(),
                m.getStartDate(),
                m.getEndDate(),
                m.isActive()
        );
    }

    private MembershipPlanDto mapPlanToDto(MembershipPlan p) {
        return new MembershipPlanDto(
                p.getId(),
                p.getName(),
                p.getDurationDays(),
                p.getPrice(),
                p.getDescription()
        );
    }
}