package com.crm.service.impl;



import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.entity.Offer;
import com.crm.repo.OfferRepository;
import com.crm.service.OfferService;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository repository;

    public OfferServiceImpl(OfferRepository repository) {
        this.repository = repository;
    }

    @Override
    public Offer addOffer(Offer offer) {
        return repository.save(offer);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return repository.save(offer);
    }

    @Override
    public void deleteOffer(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Offer> getAllOffers() {
        return repository.findAll();
    }

    @Override
    public Offer getOfferById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
