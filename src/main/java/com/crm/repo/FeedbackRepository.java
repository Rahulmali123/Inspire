package com.crm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crm.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Basic CRUD is enough for now
	
	 @Query("SELECT COALESCE(AVG(f.rating), 0) FROM Feedback f")
	    double getAverageRating();
}