package com.crm.service;

import java.util.List;

import com.crm.dto.FeedbackStatsDto;
import com.crm.entity.Feedback;

public interface FeedbackService {
    Feedback saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedback();
    FeedbackStatsDto getFeedbackStats();
}
