package com.example.seniorProject.controllers;

import com.example.seniorProject.models.DTOs.AlertRequest;
import com.example.seniorProject.models.DTOs.InputGptMessage;
import com.example.seniorProject.models.DTOs.InputMessage;
import com.example.seniorProject.services.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessagingService messagingService;

    @MessageMapping("/chat")
    public void incomingMessage(InputMessage inputMessage, Authentication authentication) {
        messagingService.processIncomingMessage(inputMessage, authentication.getName());
    }

    @MessageMapping("/gpt")
    public void incomingMessageToGpt(InputGptMessage inputGptMessage, Authentication authentication) {
        messagingService.processChatGptMessage(inputGptMessage, authentication.getName());
    }

    @MessageMapping("/alert")
    public void crisisAlert(AlertRequest alertRequest, Authentication authentication) {
        messagingService.sendCrisisAlert(alertRequest, authentication.getName());
    }
}
