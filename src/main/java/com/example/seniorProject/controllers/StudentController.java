package com.example.seniorProject.controllers;

import com.example.seniorProject.models.DTOs.AuthenticationRequest;
import com.example.seniorProject.models.DTOs.AuthenticationResponse;
import com.example.seniorProject.models.DTOs.RegistrationRequest;
import com.example.seniorProject.models.Message;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(studentService.register(registrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(studentService.authenticate(authenticationRequest));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessionsOfCounselor(Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(studentService.getAllSessionsOfStudent(authentication.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        studentService.logout(authentication.getName());
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/chat/{counselorId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Integer counselorId, Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(studentService.getAllMessages(counselorId, authentication.getName()));
    }
}
