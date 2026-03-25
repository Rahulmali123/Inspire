package com.crm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crm.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> 
{
	
	  @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
	    double sumTotalAmount();
	
}