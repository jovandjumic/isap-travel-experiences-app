package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.Specification.ExperienceSpecifications;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<Experience> getAllExperiences(Pageable pageable) {
        return experienceRepository.findAll(pageable);
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

    public Page<Experience> searchExperiences(String location, Integer minDays, Integer maxDays,
                                              Double minCost, Double maxCost,
                                              Double minRating, Double maxRating, Pageable pageable) {
        Specification<Experience> specification = Specification.where(null);

        if (location != null && !location.isEmpty()) {
            specification = specification.and(ExperienceSpecifications.hasLocation(location));
        }

        if (minDays != null && maxDays != null) {
            specification = specification.and(ExperienceSpecifications.hasDaysSpentBetween(minDays, maxDays));
        }

        if (minCost != null && maxCost != null) {
            specification = specification.and(ExperienceSpecifications.hasTotalCostBetween(minCost, maxCost));
        }

        if (minRating != null && maxRating != null) {
            specification = specification.and(ExperienceSpecifications.hasRatingBetween(minRating, maxRating));
        }

        return experienceRepository.findAll(specification, pageable);
    }
}
