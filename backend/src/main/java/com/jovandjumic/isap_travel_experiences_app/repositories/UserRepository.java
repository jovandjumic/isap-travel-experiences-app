package com.jovandjumic.isap_travel_experiences_app.repositories;

import com.jovandjumic.isap_travel_experiences_app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}