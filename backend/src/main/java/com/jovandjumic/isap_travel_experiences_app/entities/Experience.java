package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;

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

    private int daysSpent;

    @OneToOne(cascade = CascadeType.ALL)
    private Costs costs;

    private double rating;
    private Date publishDate;

    @OneToMany(mappedBy = "experience")
    private List<Comment> comments;

    private int inappropriateContentReports;

    public void setId(Long id) {
        this.id = id;
    }
}
