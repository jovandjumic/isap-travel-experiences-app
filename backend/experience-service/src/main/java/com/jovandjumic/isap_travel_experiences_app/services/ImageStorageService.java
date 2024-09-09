package com.jovandjumic.isap_travel_experiences_app.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadImage(MultipartFile file, String fileName);
    String getImageUrl(String fileName);
    void deleteImage(String imageUrl); // New method for deleting images
}
