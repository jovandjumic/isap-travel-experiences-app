package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.Specification.ExperienceSpecifications;
import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.repositories.CountryRepository;
import com.jovandjumic.isap_travel_experiences_app.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private RestTemplate restTemplate; // Dodaj RestTemplate za komunikaciju sa user-service


    public Experience createExperience(Experience experience, Long userId) {
        experience.setUserId(userId);
        experience.setCreatedAt(new Date());
        experience.setComments(new ArrayList<>());

        if (experience.getDestination().getCountryId() != null) {
            // Pronađi državu na osnovu ID-a samo ako je potrebna
            Country country = countryRepository.findById(experience.getDestination().getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Country not found with ID: " + experience.getDestination().getCountryId()));
            // U Destination čuvaš samo countryId
            experience.getDestination().setCountryId(country.getId());
        } else {
            throw new IllegalArgumentException("Country ID cannot be null");
        }

        experience.calculateCostPerPerson();
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

    public Experience updateExperience(Long id, Experience updatedExperience, Long userId) {
        return experienceRepository.findById(id).map(existingExperience -> {
            if (!existingExperience.getUserId().equals(userId)) {
                throw new AccessDeniedException("You do not have permission to update this experience");
            }
            if (updatedExperience.getDestination() != null) {
                existingExperience.setDestination(updatedExperience.getDestination());
            }
            if (updatedExperience.getDaysSpent() != null) {
                existingExperience.setDaysSpent(updatedExperience.getDaysSpent());
            }
            if (updatedExperience.getCosts() != null) {
                existingExperience.setCosts(updatedExperience.getCosts());
            }
            if (updatedExperience.getLikes() != null) {
                existingExperience.setLikes(updatedExperience.getLikes());
            }
            if (updatedExperience.getNumberOfPeople() != null) {
                existingExperience.setNumberOfPeople(updatedExperience.getNumberOfPeople());
            }
            if (updatedExperience.getDescription() != null) {
                existingExperience.setDescription(updatedExperience.getDescription());
            }
            existingExperience.calculateCostPerPerson();
            return experienceRepository.save(existingExperience);
        }).orElse(null);
    }

    public void deleteExperience(Long id, Long userId) {
        Experience existingExperience = experienceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        if (!existingExperience.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this experience");
        }

        if(existingExperience.getImages() != null) {
            for (String imageUrl : existingExperience.getImages()) {
                imageStorageService.deleteImage(imageUrl);
            }
        }

        experienceRepository.deleteById(id);
    }

    public Page<Experience> searchExperiences(Long userId,
                                              String locationName,
                                              String regionArea,
                                              Long countryId,
                                              String continent,
                                              String locationType,
                                              Integer minDays, Integer maxDays,
                                              Double minCost, Double maxCost,
                                              Double minCostPerPerson, Double maxCostPerPerson,
                                              Pageable pageable) {
        Specification<Experience> specification = Specification.where(null);

        if (userId != null) {
            specification = specification.and(ExperienceSpecifications.hasUser(userId));
        }

        if (locationName != null && !locationName.isEmpty()) {
            specification = specification.and(ExperienceSpecifications.hasLocationName(locationName));
        }

        if (regionArea != null && !regionArea.isEmpty()) {
            specification = specification.and(ExperienceSpecifications.hasRegionArea(regionArea));
        }

        if (countryId != null) {
            specification = specification.and(ExperienceSpecifications.hasCountryId(countryId));
        }

        if (continent != null && !continent.isEmpty()) {
            specification = specification.and(ExperienceSpecifications.hasContinent(continent));
        }

        if (locationType != null && !locationType.isEmpty()) {
            specification = specification.and(ExperienceSpecifications.hasLocationType(locationType));
        }

        if (minDays != null) {
            specification = specification.and(ExperienceSpecifications.hasMinDaysSpent(minDays));
        }

        if (maxDays != null) {
            specification = specification.and(ExperienceSpecifications.hasMaxDaysSpent(maxDays));
        }

        if (minCost != null) {
            specification = specification.and(ExperienceSpecifications.hasMinTotalCost(minCost));
        }

        if (maxCost != null) {
            specification = specification.and(ExperienceSpecifications.hasMaxTotalCost(maxCost));
        }

        if (minCostPerPerson != null) {
            specification = specification.and(ExperienceSpecifications.hasMinCostPerPerson(minCostPerPerson));
        }

        if (maxCostPerPerson != null) {
            specification = specification.and(ExperienceSpecifications.hasMaxCostPerPerson(maxCostPerPerson));
        }

        return experienceRepository.findAll(specification, pageable);
    }

    public void addImageToExperience(Long experienceId, String imageUrl) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));
        experience.addImage(imageUrl);
        experienceRepository.save(experience);
    }

    public void removeImageFromExperience(Long experienceId, String imageUrl) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));
        experience.removeImage(imageUrl);
        experienceRepository.save(experience);
    }

    public Experience likeExperience(Long experienceId, Long userId) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        if (experience.getLikedByUsersIds().contains(userId)) {
            experience.getLikedByUsersIds().remove(userId);
            experience.setLikes(experience.getLikes() - 1);
        } else {
            experience.getLikedByUsersIds().add(userId);
            experience.setLikes(experience.getLikes() + 1);
        }

        return experienceRepository.save(experience);
    }
}
