package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.repositories.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination updateDestination(Long id, Destination updatedDestination) {
        Optional<Destination> existingDestination = destinationRepository.findById(id);
        if (existingDestination.isPresent()) {
            updatedDestination.setId(id);
            return destinationRepository.save(updatedDestination);
        }
        return null;
    }

    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
