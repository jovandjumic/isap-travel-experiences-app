package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Destination destination;

    @Setter
    @Getter
    private int daysSpent;

    @OneToOne(cascade = CascadeType.ALL)
    private Costs costs;

    private double rating;
    private Date publishDate;

    @OneToMany(mappedBy = "experience")
    private List<Comment> comments;

    private int inappropriateContentReports;

}
