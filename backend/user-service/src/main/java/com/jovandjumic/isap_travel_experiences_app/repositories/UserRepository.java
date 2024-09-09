package com.jovandjumic.isap_travel_experiences_app.repositories;

import com.jovandjumic.isap_travel_experiences_app.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}