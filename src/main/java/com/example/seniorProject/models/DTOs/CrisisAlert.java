package com.example.seniorProject.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrisisAlert {
    private String studentUsername;
    private String studentFirstName;
    private String studentLastName;
    private String triggerMessage;
    private String timestamp;
}
