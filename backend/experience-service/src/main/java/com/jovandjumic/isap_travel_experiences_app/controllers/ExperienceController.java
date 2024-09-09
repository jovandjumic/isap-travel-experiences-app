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

    private Long getUserIdFromToken(String token) {
        String url = "http://user-service:8081/api/auth/me";  // Poziv prema user-service
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);  // Dodaj token u Authorization zaglavlje
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return Long.valueOf(responseBody.get("id").toString());  // Pretpostavka je da vraća userId ili id
    }

}
