package com.example.seniorProject.services;

import com.example.seniorProject.config.JwtService;
import com.example.seniorProject.mappers.CounselorMapper;
import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.DTOs.AuthenticationRequest;
import com.example.seniorProject.models.DTOs.AuthenticationResponse;
import com.example.seniorProject.models.DTOs.CounselorDTO;
import com.example.seniorProject.models.DTOs.RegistrationRequest;
import com.example.seniorProject.models.Message;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.models.Student;
import com.example.seniorProject.models.enums.Roles;
import com.example.seniorProject.repositories.CounselorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CounselorMapper counselorMapper;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        Counselor counselor = counselorMapper.RegistrationRequestToCounselor(registrationRequest);
        counselor.setPassword(passwordEncoder.encode(counselor.getPassword()));
        counselor.setRole(Roles.ROLE_COUNSELOR);
        counselorRepository.save(counselor);

        String jwtToken = jwtService.generateToken(counselor);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        Counselor counselor = counselorRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(counselor);
        counselor.setOnline(true);
        counselorRepository.save(counselor);
        return new AuthenticationResponse(jwtToken);
    }

    public Counselor getCounselor(String username) {
        return counselorRepository.findByUsername(username).orElseThrow();
    }

    public CounselorDTO getCounselorDTObyId(int id) {
        Counselor counselor = counselorRepository.findById(id).orElseThrow();
        return counselorMapper.CounselorToDTO(counselor);
    }

    public boolean isFree(String username, String roomId, LocalDateTime startTime) {
        Counselor counselor = counselorRepository.findByUsername(username).orElseThrow();
        LocalDateTime lower = startTime.minusHours(1);
        LocalDateTime upper = startTime.plusHours(1);
        return counselorRepository.clashingSessions(counselor.getId(), roomId, lower, upper).isEmpty();
    }

    public List<Session> getAllSessionsOfCounselor(String username) {
        Counselor counselor = counselorRepository.findByUsername(username).orElseThrow();
        return counselor.getSessions();
    }

    public List<CounselorDTO> getAllCounselorsDTO() {
        List<Counselor> counselors = counselorRepository.findAll();
        List<CounselorDTO> counselorDTOs = new ArrayList<>();
        counselors.forEach(counselor -> {counselorDTOs.add(counselorMapper.CounselorToDTO(counselor));});
        return counselorDTOs;
    }

    public void logout(String username) {
        Counselor counselor = counselorRepository.findByUsername(username).orElseThrow();
        counselor.setOnline(false);
        counselorRepository.save(counselor);
    }

    public List<Message> getChatMessages(Integer studentId, String username) {
        Counselor counselor = counselorRepository.findByUsername(username).orElseThrow();
        return counselor.getMessages().stream().filter(m -> m.getStudent().getId() == studentId).collect(Collectors.toList());
    }

}
