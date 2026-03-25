package com.crm.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.MembershipPlan;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
}
