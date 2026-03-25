package com.crm.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crm.dto.CustomerMembershipDto;
import com.crm.dto.MembershipPlanDto;
import com.crm.service.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/membership")

@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService service;

    // --- Create Plan ---
    @PostMapping("/plan")
    public ResponseEntity<MembershipPlanDto> createPlan(@RequestBody MembershipPlanDto dto) {
        return ResponseEntity.ok(service.createPlan(dto));
    }

    // --- Get Plans ---
    @GetMapping("/plan")
    public ResponseEntity<List<MembershipPlanDto>> getPlans() {
        return ResponseEntity.ok(service.getAllPlans());
    }
    
    
    // --- Assign Membership ---
    @PostMapping("/assign")
    public ResponseEntity<CustomerMembershipDto> assign(
            @Valid @RequestBody CustomerMembershipDto dto) {
        return ResponseEntity.ok(service.assignMembership(dto));
    }

    // --- Renew ---
    @PutMapping("/{id}/renew")
    public ResponseEntity<CustomerMembershipDto> renew(@PathVariable Long id) {
        return ResponseEntity.ok(service.renewMembership(id));
    }

    // --- Upgrade ---
    @PutMapping("/{id}/upgrade")
    public ResponseEntity<CustomerMembershipDto> upgrade(
            @PathVariable Long id,
            @RequestParam Long planId) {
        return ResponseEntity.ok(service.upgradeMembership(id, planId));
    }

    // --- Get by Customer ---
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerMembershipDto>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getMembershipsByCustomer(customerId));
    }
}
