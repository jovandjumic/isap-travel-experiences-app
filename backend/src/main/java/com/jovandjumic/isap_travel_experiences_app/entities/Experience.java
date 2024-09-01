package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne(cascade = CascadeType.ALL)
    private Destination destination;

    @Setter
    @Getter
    private Integer daysSpent;

    @OneToOne(cascade = CascadeType.ALL)
    private Costs costs;

    private Integer likes = 0;

    private Date createdAt;

    private Integer numberOfPeople;

    @OneToMany(mappedBy = "experience", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name= "experience_liked_by_users",
            joinColumns = @JoinColumn(name = "experience_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id")
    )
    List<AppUser> likedByUsers = new ArrayList<>();

    @ElementCollection
    private List<String> images = new ArrayList<>();

    private String description;

    private Integer inappropriateContentReports = 0;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    public void addImage(String imageUrl) {
        images.add(imageUrl);
    }

    public void removeImage(String imageUrl) {
        images.remove(imageUrl);
    }

    @Transient
    public Double getCostPerPerson() {
        if (this.costs == null || this.costs.getTotalCost() == null || this.numberOfPeople == null || this.numberOfPeople == 0) {
            return null;
        }
        return this.costs.getTotalCost() / this.numberOfPeople;
    }

}
