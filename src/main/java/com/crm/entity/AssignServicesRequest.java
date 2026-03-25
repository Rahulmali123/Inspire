package com.crm.entity;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignServicesRequest {
    private Long customerId;
    private Set<Long> serviceIds;
}