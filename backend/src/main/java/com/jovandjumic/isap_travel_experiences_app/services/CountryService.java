package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

}
