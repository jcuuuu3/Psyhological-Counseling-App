package com.example.seniorProject.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
public class OutputGptMessage {

    private String text;
}
