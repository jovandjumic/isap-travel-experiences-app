package com.jovandjumic.isap_travel_experiences_app.repositories;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
