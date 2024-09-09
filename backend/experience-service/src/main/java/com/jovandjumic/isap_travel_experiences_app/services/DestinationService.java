package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.repositories.CountryRepository;
import com.jovandjumic.isap_travel_experiences_app.repositories.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private CountryRepository countryRepository;

    // Dohvati destinaciju po ID-u
    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    // Vrati sve destinacije
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    // Kreiraj novu destinaciju sa countryId
    public Destination createDestination(Destination destination) {
        if (destination.getCountryId() != null) {
            Long countryId = destination.getCountryId();
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new IllegalArgumentException("Country not found with ID: " + countryId));
            destination.setCountryId(country.getId()); // Postavi ID države
        } else {
            throw new IllegalArgumentException("Country ID cannot be null");
        }
        return destinationRepository.save(destination);
    }

    // Ažuriraj postojeću destinaciju sa countryId
    public Destination updateDestination(Long id, Destination updatedDestination) {
        return destinationRepository.findById(id).map(existingDestination -> {
            if (updatedDestination.getLocationName() != null) {
                existingDestination.setLocationName(updatedDestination.getLocationName());
            }
            if (updatedDestination.getLocationType() != null) {
                existingDestination.setLocationType(updatedDestination.getLocationType());
            }
            if (updatedDestination.getRegionArea() != null) {
                existingDestination.setRegionArea(updatedDestination.getRegionArea());
            }
            if (updatedDestination.getCountryId() != null) {
                Long countryId = updatedDestination.getCountryId();
                Country country = countryRepository.findById(countryId)
                        .orElseThrow(() -> new IllegalArgumentException("Country not found with ID: " + countryId));
                existingDestination.setCountryId(country.getId());
            }
            return destinationRepository.save(existingDestination);
        }).orElseThrow(() -> new IllegalArgumentException("Destination not found with ID: " + id));
    }

    // Obrisi destinaciju po ID-u
    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
}
