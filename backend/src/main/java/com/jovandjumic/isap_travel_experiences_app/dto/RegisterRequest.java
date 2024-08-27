package com.jovandjumic.isap_travel_experiences_app.dto;

import com.jovandjumic.isap_travel_experiences_app.enums.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
}