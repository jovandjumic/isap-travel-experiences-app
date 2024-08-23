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
        return userRepository.findById(id).map(existingAppUser -> {
            if (updatedAppUser.getUsername() != null) {
                existingAppUser.setUsername(updatedAppUser.getUsername());
            }
            if (updatedAppUser.getPassword() != null) {
                existingAppUser.setPassword(updatedAppUser.getPassword());
            }
            if (updatedAppUser.getEmail() != null) {
                existingAppUser.setEmail(updatedAppUser.getEmail());
            }
            if (updatedAppUser.getFirstName() != null) {
                existingAppUser.setFirstName(updatedAppUser.getFirstName());
            }
            if (updatedAppUser.getLastName() != null) {
                existingAppUser.setLastName(updatedAppUser.getLastName());
            }
            if (updatedAppUser.getPhoneNumber() != null) {
                existingAppUser.setPhoneNumber(updatedAppUser.getPhoneNumber());
            }
            return userRepository.save(existingAppUser);
        }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
