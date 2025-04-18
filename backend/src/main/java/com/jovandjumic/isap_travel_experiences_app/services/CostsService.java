package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Costs;
import com.jovandjumic.isap_travel_experiences_app.repositories.CostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CostsService {

    @Autowired
    private CostsRepository costsRepository;

    public Costs createCosts(Costs costs) {
        return costsRepository.save(costs);
    }

    public Optional<Costs> getCostsById(Long id) {
        return costsRepository.findById(id);
    }

    public List<Costs> getAllCosts() {
        return costsRepository.findAll();
    }

    public Costs updateCosts(Long id, Costs updatedCosts) {
        return costsRepository.findById(id).map(existingCosts -> {
            if (updatedCosts.getTravelCost() != null) {
                existingCosts.setTravelCost(updatedCosts.getTravelCost());
            }
            if (updatedCosts.getAccommodationCost() != null) {
                existingCosts.setAccommodationCost(updatedCosts.getAccommodationCost());
            }
            if (updatedCosts.getOtherCosts() != null) {
                existingCosts.setOtherCosts(updatedCosts.getOtherCosts());
            }
            return costsRepository.save(existingCosts);
        }).orElse(null);
    }

    public void deleteCosts(Long id) {
        costsRepository.deleteById(id);
    }
}
