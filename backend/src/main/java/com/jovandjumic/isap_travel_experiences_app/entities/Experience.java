package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    private Double rating;
    private Date createdAt;

    @OneToMany(mappedBy = "experience")
    private List<Comment> comments;

    private Integer inappropriateContentReports;

}
