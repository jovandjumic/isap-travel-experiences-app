package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    public Experience createExperience(Experience experience) {
        return experienceRepository.save(experience);
    }

    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    public Experience updateExperience(Long id, Experience updatedExperience) {
        return experienceRepository.findById(id).map(existingExperience -> {
            if (updatedExperience.getDestination() != null) {
                existingExperience.setDestination(updatedExperience.getDestination());
            }
            if (updatedExperience.getDaysSpent() != null) {
                existingExperience.setDaysSpent(updatedExperience.getDaysSpent());
            }
            if (updatedExperience.getCosts() != null) {
                existingExperience.setCosts(updatedExperience.getCosts());
            }
            if (updatedExperience.getRating() != null) {
                existingExperience.setRating(updatedExperience.getRating());
            }
            return experienceRepository.save(existingExperience);
        }).orElse(null);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }
}
