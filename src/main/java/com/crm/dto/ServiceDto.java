package com.crm.dto;



import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ServiceDto {

    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer duration;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean isActive;
}