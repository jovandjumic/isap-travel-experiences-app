package com.jovandjumic.isap_travel_experiences_app.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private AppUser appUser;

    @ManyToOne
    private Experience experience;

    private Date commentDate;

}
