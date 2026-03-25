package com.crm.service;

import java.util.List;

import com.crm.dto.CustomerMembershipDto;
import com.crm.dto.MembershipPlanDto;

public interface MembershipService {

    // Plan
    MembershipPlanDto createPlan(MembershipPlanDto dto);
    List<MembershipPlanDto> getAllPlans();

    // Customer Membership
    CustomerMembershipDto assignMembership(CustomerMembershipDto dto);
    CustomerMembershipDto renewMembership(Long id);
    CustomerMembershipDto upgradeMembership(Long id, Long newPlanId);

    List<CustomerMembershipDto> getMembershipsByCustomer(Long customerId);
}