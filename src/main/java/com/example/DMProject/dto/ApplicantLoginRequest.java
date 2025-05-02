package com.example.DMProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record ApplicantLoginRequest(
        @NotNull(message="Customer email is required")
        @Email(message = "Email must be in correct format")
        @JsonProperty("username")
        String name,

        @NotNull(message = "Password should be present")
        @NotEmpty(message = "Password should be present")
        @NotBlank(message = "Password should be present")
        @Size(min = 6, max = 12)
        @JsonProperty("password")
        String password
) {
}