package com.crm.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.entity.Offer;
import com.crm.service.OfferService;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService service;

    public OfferController(OfferService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Offer addOffer(@RequestBody Offer offer) {
        return service.addOffer(offer);
    }

    @PutMapping("/update")
    public Offer updateOffer(@RequestBody Offer offer) {
        return service.updateOffer(offer);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOffer(@PathVariable Long id) {
        service.deleteOffer(id);
    }

    @GetMapping("/all")
    public List<Offer> getAllOffers() {
        return service.getAllOffers();
    }

    @GetMapping("/{id}")
    public Offer getOffer(@PathVariable Long id) {
        return service.getOfferById(id);
    }
}