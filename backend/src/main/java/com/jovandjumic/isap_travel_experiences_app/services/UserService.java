package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.AppUser;
import com.jovandjumic.isap_travel_experiences_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public AppUser createUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser updateUser(Long id, AppUser updatedAppUser) {
        return userRepository.findById(id).map(existingUser -> {
            if (updatedAppUser.getUsername() != null) {
                existingUser.setUsername(updatedAppUser.getUsername());
            }
            if (updatedAppUser.getPassword() != null) {
                existingUser.setPassword(updatedAppUser.getPassword());
            }
            if (updatedAppUser.getEmail() != null) {
                existingUser.setEmail(updatedAppUser.getEmail());
            }
            if (updatedAppUser.getFirstName() != null) {
                existingUser.setFirstName(updatedAppUser.getFirstName());
            }
            if (updatedAppUser.getLastName() != null) {
                existingUser.setLastName(updatedAppUser.getLastName());
            }
            if (updatedAppUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedAppUser.getPhoneNumber());
            }
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
