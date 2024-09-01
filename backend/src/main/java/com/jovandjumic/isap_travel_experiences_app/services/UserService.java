package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.AppUser;
import com.jovandjumic.isap_travel_experiences_app.repositories.UserRepository;
import com.jovandjumic.isap_travel_experiences_app.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
            if (updatedAppUser.getProfilePicture() != null) {
                existingAppUser.setProfilePicture(updatedAppUser.getProfilePicture());
            }
            if (updatedAppUser.getBiography() != null) {
                existingAppUser.setBiography(updatedAppUser.getBiography());
            }
            return userRepository.save(existingAppUser);
        }).orElse(null);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var appUser = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), appUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(appUser);
    }

    public AppUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (AppUser) principal;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
