package com.jovandjumic.isap_travel_experiences_app.repositories;

import com.jovandjumic.isap_travel_experiences_app.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t JOIN t.appUser au WHERE au.id = :id AND (t.expired = FALSE OR t.revoked = FALSE)")
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}