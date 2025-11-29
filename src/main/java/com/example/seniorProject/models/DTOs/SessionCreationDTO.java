package com.example.seniorProject.models.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionCreationDTO {

    private LocalDateTime startTime;
    @NotBlank
    private String roomNumber;
}
