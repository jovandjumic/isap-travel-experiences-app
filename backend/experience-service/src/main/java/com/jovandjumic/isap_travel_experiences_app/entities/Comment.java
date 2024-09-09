package com.jovandjumic.isap_travel_experiences_app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    // Umesto direktne reference na AppUser, koristi se userId
    private Long userId;

    @ManyToOne
    @JsonIgnore
    private Experience experience;

    private Date commentDate;
}
