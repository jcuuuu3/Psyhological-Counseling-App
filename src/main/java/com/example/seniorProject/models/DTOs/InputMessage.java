package com.example.seniorProject.models.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class InputMessage {

    @NotNull
    private String recipientUsername;

    @NotNull
    private String text;

    private boolean fromCounselor;
}
