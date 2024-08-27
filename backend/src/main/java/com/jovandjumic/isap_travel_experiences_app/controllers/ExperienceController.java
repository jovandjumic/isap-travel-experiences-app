package com.jovandjumic.isap_travel_experiences_app.controllers;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.services.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<Experience> createExperience(@RequestBody Experience experience) {
        Experience createdExperience = experienceService.createExperience(experience);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable Long id) {
        Optional<Experience> experience = experienceService.getExperienceById(id);
        return experience.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable Long id, @RequestBody Experience experience) {
        Experience updatedExperience = experienceService.updateExperience(id, experience);
        return updatedExperience != null ? ResponseEntity.ok(updatedExperience)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public Page<Experience> getAllExperiences(Pageable pageable) {
        return experienceService.getAllExperiences(pageable);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<String> addImageToExperience(@PathVariable Long id, @RequestBody String imageUrl) {
        experienceService.addImageToExperience(id, imageUrl);
        return ResponseEntity.ok("Image added successfully.");
    }

    @DeleteMapping("/{id}/images")
    public ResponseEntity<String> removeImageFromExperience(@PathVariable Long id, @RequestBody String imageUrl) {
        experienceService.removeImageFromExperience(id, imageUrl);
        return ResponseEntity.ok("Image removed successfully.");
    }

    @GetMapping("/search")
    public Page<Experience> searchExperiences(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minDays,
            @RequestParam(required = false) Integer maxDays,
            @RequestParam(required = false) Double minCost,
            @RequestParam(required = false) Double maxCost,
            @RequestParam(required = false) Double minCostPerPerson,
            @RequestParam(required = false) Double maxCostPerPerson,
            Pageable pageable) {
        return experienceService.searchExperiences(location, minDays, maxDays, minCost, maxCost, minCostPerPerson, maxCostPerPerson, pageable);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long id) {
        boolean liked = experienceService.toggleLikeExperience(id);
        String message = liked ? "Experience liked." : "Like removed.";
        return ResponseEntity.ok(message);
    }

}
