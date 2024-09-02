package com.jovandjumic.isap_travel_experiences_app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalImageStorageService implements ImageStorageService {

    @Value("${image.storage.path}")
    private String storagePath;

    @Override
    public String uploadImage(MultipartFile file, String fileName) {
        try {
            Path directoryPath = Paths.get(storagePath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file locally", e);
        }
    }

    @Override
    public String getImageUrl(String fileName) {
        return fileName;
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            Path imagePath = Paths.get(imageUrl).toAbsolutePath().normalize();
            System.out.println("Attempting to delete image at path: " + imagePath.toString());
            Files.deleteIfExists(imagePath);
            System.out.println("Image deleted successfully.");
        } catch (IOException e) {
            System.err.println("Failed to delete image: " + e.getMessage());
            throw new RuntimeException("Failed to delete image from local storage", e);
        }
    }


}
