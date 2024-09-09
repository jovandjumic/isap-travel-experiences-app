package com.jovandjumic.isap_travel_experiences_app.controllers;

import com.jovandjumic.isap_travel_experiences_app.entities.Costs;
import com.jovandjumic.isap_travel_experiences_app.services.CostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/costs")
public class CostsController {

    @Autowired
    private CostsService costsService;

    @PostMapping
    public ResponseEntity<Costs> createCosts(@RequestBody Costs costs) {
        Costs createdCosts = costsService.createCosts(costs);
        return new ResponseEntity<>(createdCosts, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Costs> getCostsById(@PathVariable Long id) {
        Optional<Costs> costs = costsService.getCostsById(id);
        return costs.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Costs> updateCosts(@PathVariable Long id, @RequestBody Costs costs) {
        Costs updatedCosts = costsService.updateCosts(id, costs);
        return updatedCosts != null ? ResponseEntity.ok(updatedCosts)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCosts(@PathVariable Long id) {
        costsService.deleteCosts(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Costs>> getAllCosts() {
        List<Costs> costsList = costsService.getAllCosts();
        return new ResponseEntity<>(costsList, HttpStatus.OK);
    }
}

