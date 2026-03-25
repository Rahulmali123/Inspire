package com.crm.service.impl;



import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.dto.FeedbackStatsDto;
import com.crm.entity.Feedback;
import com.crm.repo.FeedbackRepository;
import com.crm.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public FeedbackStatsDto getFeedbackStats() {
        long totalFeedbacks = feedbackRepository.count();
        double averageRating = feedbackRepository.getAverageRating();
        return new FeedbackStatsDto(totalFeedbacks, Math.round(averageRating * 10.0) / 10.0); // round to 1 decimal
    }
}