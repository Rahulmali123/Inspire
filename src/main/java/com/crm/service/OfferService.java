package com.crm.service;

import java.util.List;

import com.crm.entity.Offer;

public interface OfferService {
    Offer addOffer(Offer offer);
    Offer updateOffer(Offer offer);
    void deleteOffer(Long id);
    List<Offer> getAllOffers();
    Offer getOfferById(Long id);
}