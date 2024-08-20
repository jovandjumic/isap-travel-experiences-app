package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;

@Entity
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationName;
    private String locationType;
    private String regionArea;

    @ManyToOne
    private Country country;

    public void setId(Long id) {
        this.id = id;
    }
}
