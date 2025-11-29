package com.example.seniorProject.controllers;

import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.DTOs.SessionCreationDTO;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.services.CounselorService;
import com.example.seniorProject.services.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/session")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<SessionCreationDTO> createSession(@Valid @RequestBody SessionCreationDTO sessionCreationDTO, Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_COUNSELOR"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(sessionService.createSession(sessionCreationDTO, authentication.getName()));
    }



    @PostMapping("/book/{sessionId}")
    public ResponseEntity<String> bookSession(@PathVariable int sessionId, Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        sessionService.bookSession(sessionId, authentication.getName());
        return ResponseEntity.ok("Booked session");
    }

    @PostMapping("/unbook/{sessionId}")
    public ResponseEntity<String> unbookSession(@PathVariable int sessionId, Authentication authentication) {
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));
        if(!hasAccess){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        sessionService.unbookSession(sessionId, authentication.getName());
        return ResponseEntity.ok("Unbooked session");
    }
}
