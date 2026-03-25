package com.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackStatsDto
{
    private long totalFeedbacks;
    private double averageRating;
}