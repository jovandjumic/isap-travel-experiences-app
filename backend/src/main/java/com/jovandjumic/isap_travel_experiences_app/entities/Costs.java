package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Costs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Double travelCost;
    private String travelMode;
    private String travelRoute;
    private String travelCostType; // "Per person" or "Total"

    private Double accommodationCost;
    private String accommodationCostType; // "Per person" or "Total"

    private Double otherCosts;
    private String otherCostsType; // "Per person" or "Total"

    private Double totalCost;

    @PrePersist
    @PreUpdate
    private void calculateTotalCost() {
        this.totalCost = (travelCost != null ? travelCost : 0.0) +
                (accommodationCost != null ? accommodationCost : 0.0) +
                (otherCosts != null ? otherCosts : 0.0);
    }
}
