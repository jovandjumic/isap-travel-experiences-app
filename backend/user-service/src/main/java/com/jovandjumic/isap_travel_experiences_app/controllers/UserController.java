package com.jovandjumic.isap_travel_experiences_app.controllers;

import com.jovandjumic.isap_travel_experiences_app.entities.AppUser;
import com.jovandjumic.isap_travel_experiences_app.dto.ChangePasswordRequest;
import com.jovandjumic.isap_travel_experiences_app.services.ImageStorageService;
import com.jovandjumic.isap_travel_experiences_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageStorageService imageStorageService;

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        AppUser createdAppUser = userService.createUser(appUser);
        return new ResponseEntity<>(createdAppUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        Optional<AppUser> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser appUser) {
        AppUser updatedAppUser = userService.updateUser(id, appUser);
        return updatedAppUser != null ? ResponseEntity.ok(updatedAppUser)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageStorageService.uploadImage(file, file.getOriginalFilename());

            userService.uploadProfilePicture(id, imageUrl);

            return ResponseEntity.ok("Image added successfully: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/profile-picture")
    public ResponseEntity<String> removeProfilePicture(@PathVariable Long id) {
        try {
            // Prvo dohvatite korisnika preko ID-a
            Optional<AppUser> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            AppUser user = userOptional.get();
            String imageUrl = user.getProfilePicture(); // Preuzmite URL trenutne profilne slike

            // Proverite da li korisnik ima postavljenu profilnu sliku
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Prvo obrišite sliku iz skladišta
                imageStorageService.deleteImage(imageUrl);

                // Zatim obrišite referencu na sliku iz korisnikovog entiteta
                userService.removeProfilePicture(id);

                return ResponseEntity.ok("Profile picture removed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No profile picture to remove.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to remove image: " + e.getMessage());
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> appUsers = userService.getAllUsers();
        return new ResponseEntity<>(appUsers, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}

