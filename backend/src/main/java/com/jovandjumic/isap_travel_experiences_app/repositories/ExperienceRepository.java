package com.jovandjumic.isap_travel_experiences_app.repositories;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

}