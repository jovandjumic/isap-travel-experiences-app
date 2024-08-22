package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Costs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private double travelCost;
    private String travelMode;
    private String travelRoute;
    private String travelCostType; // "Per person" or "Total"

    private double accommodationCost;
    private String accommodationCostType; // "Per person" or "Total"

    private double otherCosts;
    private String otherCostsType; // "Per person" or "Total"

}
