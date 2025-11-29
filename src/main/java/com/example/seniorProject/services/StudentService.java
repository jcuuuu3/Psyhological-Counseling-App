package com.example.seniorProject.services;

import com.example.seniorProject.config.JwtService;
import com.example.seniorProject.mappers.StudentMapper;
import com.example.seniorProject.models.DTOs.AuthenticationRequest;
import com.example.seniorProject.models.DTOs.AuthenticationResponse;
import com.example.seniorProject.models.DTOs.RegistrationRequest;
import com.example.seniorProject.models.Message;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.models.Student;
import com.example.seniorProject.models.enums.Roles;
import com.example.seniorProject.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StudentMapper studentMapper;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        Student student = studentMapper.RegistrationRequestToStudent(registrationRequest);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setRole(Roles.ROLE_STUDENT);
        studentRepository.save(student);

        String jwtToken = jwtService.generateToken(student);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            )
        );
        Student student = studentRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(student);
        student.setOnline(true);
        studentRepository.save(student);
        return new AuthenticationResponse(jwtToken);
    }

    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username).orElseThrow();
    }

    public List<Session> getAllSessionsOfStudent(String username) {
        Student student = studentRepository.findByUsername(username).orElseThrow();
        return student.getSessions();
    }

    public void logout(String username) {
        Student student = studentRepository.findByUsername(username).orElseThrow();
        student.setOnline(false);
        studentRepository.save(student);
    }

    public List<Message> getAllMessages(Integer counselorId, String username) {
        Student student = studentRepository.findByUsername(username).orElseThrow();
        return student.getMessages().stream().filter(m -> m.getCounselor().getId() == counselorId).collect(Collectors.toList());
    }
}
