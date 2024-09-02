package com.jovandjumic.isap_travel_experiences_app.config;

import com.jovandjumic.isap_travel_experiences_app.services.ImageStorageService;
import com.jovandjumic.isap_travel_experiences_app.services.LocalImageStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfiguration {

    @Bean
    @Profile("dev")
    public ImageStorageService localImageStorageService() {
        return new LocalImageStorageService();
    }

//    @Bean
//    @Profile("prod")
//    public ImageStorageService s3ImageStorageService(S3Client s3Client) {
//        return new S3ImageStorageService(s3Client);
//    }
}