package com.example.seniorProject.controllers;

import com.example.seniorProject.models.DTOs.AuthenticationRequest;
import com.example.seniorProject.models.DTOs.AuthenticationResponse;
import com.example.seniorProject.models.DTOs.CounselorDTO;
import com.example.seniorProject.models.DTOs.RegistrationRequest;
import com.example.seniorProject.models.Message;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.services.CounselorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/counselor")
public class CounselorController {

    private final CounselorService counselorService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(counselorService.register(registrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(counselorService.authenticate(authenticationRequest));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessionsOfCounselor(Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_COUNSELOR"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(counselorService.getAllSessionsOfCounselor(authentication.getName()));
    }

    @GetMapping("/{counselorId}")
    public ResponseEntity<CounselorDTO> getCounselorById(@PathVariable int counselorId) {
        return ResponseEntity.ok(counselorService.getCounselorDTObyId(counselorId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CounselorDTO>> getAllCounselors() {
        return ResponseEntity.ok(counselorService.getAllCounselorsDTO());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        counselorService.logout(authentication.getName());
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/chat/{studentId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable int studentId, Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_COUNSELOR"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(counselorService.getChatMessages(studentId, authentication.getName()));
    }


}
