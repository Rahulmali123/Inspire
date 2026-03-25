package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDto 
{
    private String label;  // e.g., Date, Customer Name, or Service Name
    private Double value;  // revenue or count
}