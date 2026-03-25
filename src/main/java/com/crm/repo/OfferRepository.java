package com.crm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.entity.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

}