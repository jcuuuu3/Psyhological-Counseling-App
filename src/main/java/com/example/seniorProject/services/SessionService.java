package com.example.seniorProject.services;

import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.DTOs.SessionCreationDTO;
import com.example.seniorProject.models.Session;
import com.example.seniorProject.models.Student;
import com.example.seniorProject.models.enums.SessionStatus;
import com.example.seniorProject.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CounselorService counselorService;
    private final StudentService studentService;

    public SessionCreationDTO createSession(SessionCreationDTO sessionCreationDTO, String counselorUsername) {
        if(! counselorService.isFree(counselorUsername, sessionCreationDTO.getRoomNumber(), sessionCreationDTO.getStartTime())){
            throw new RuntimeException("Clashing session");
        }
        Counselor counselor = counselorService.getCounselor(counselorUsername);
        Session session = new Session();
        session.setCounselor(counselor);
        session.setStartTime(sessionCreationDTO.getStartTime());
        session.setRoomNumber(sessionCreationDTO.getRoomNumber());
        session.setSessionStatus(SessionStatus.CREATED);
        sessionRepository.save(session);
        return sessionCreationDTO;
    }

    public Session bookSession(int sessionId, String username) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        if(session.getSessionStatus() == SessionStatus.ASSIGNED){
            throw new RuntimeException("Session is already booked");
        }
        Student student = studentService.getStudentByUsername(username);
        session.setSessionStatus(SessionStatus.ASSIGNED);
        session.setStudent(student);
        return sessionRepository.save(session);
    }

    public Session unbookSession(int sessionId, String username) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        if(session.getSessionStatus() != SessionStatus.ASSIGNED){
            throw new RuntimeException("Session is not booked");
        }
        Student student = studentService.getStudentByUsername(username);
        if(!student.getSessions().contains(session)){
            throw new RuntimeException("Session is not booked");
        }
        session.setStudent(null);
        session.setSessionStatus(SessionStatus.CREATED);
        return sessionRepository.save(session);
    }
}
