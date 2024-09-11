package com.jovandjumic.isap_travel_experiences_app.controllers;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.services.ExperienceService;
import com.jovandjumic.isap_travel_experiences_app.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<Experience> createExperience(@RequestBody Experience experience, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        Experience createdExperience = experienceService.createExperience(experience, userId);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable Long id) {
        Optional<Experience> experience = experienceService.getExperienceById(id);
        return experience.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable Long id, @RequestBody Experience experience, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        Experience updatedExperience = experienceService.updateExperience(id, experience, userId);
        return updatedExperience != null ? ResponseEntity.ok(updatedExperience)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        experienceService.deleteExperience(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public Page<Experience> getAllExperiences(Pageable pageable) {
        return experienceService.getAllExperiences(pageable);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<String> addImageToExperience(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageStorageService.uploadImage(file, file.getOriginalFilename());

            experienceService.addImageToExperience(id, imageUrl);

            return ResponseEntity.ok("Image added successfully: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/images")
    public ResponseEntity<String> removeImageFromExperience(@PathVariable Long id, @RequestBody String imageUrl) {
        try {
            experienceService.removeImageFromExperience(id, imageUrl);

            imageStorageService.deleteImage(imageUrl);

            return ResponseEntity.ok("Image removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to remove image: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Page<Experience> searchExperiences(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String regionArea,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) String continent,
            @RequestParam(required = false) String locationType,
            @RequestParam(required = false) Integer minDays,
            @RequestParam(required = false) Integer maxDays,
            @RequestParam(required = false) Double minCost,
            @RequestParam(required = false) Double maxCost,
            @RequestParam(required = false) Double minCostPerPerson,
            @RequestParam(required = false) Double maxCostPerPerson,
            Pageable pageable) {
        return experienceService.searchExperiences(userId, locationName, regionArea, countryId, continent, locationType, minDays, maxDays, minCost, maxCost, minCostPerPerson, maxCostPerPerson, pageable);
    }

    private Long getUserIdFromToken(String token) {
        String url = "http://3.85.169.58:8080/api/auth/me";  // Poziv prema user-service
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);  // Dodaj token u Authorization zaglavlje
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return Long.valueOf(responseBody.get("id").toString());  // Pretpostavka je da vraća userId ili id
    }

}
