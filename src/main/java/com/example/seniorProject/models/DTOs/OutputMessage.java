package com.example.seniorProject.models.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OutputMessage {

    private String senderUsername;

    private LocalDateTime date;

    private String text;
}
