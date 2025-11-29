package com.example.seniorProject.mappers;

import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.DTOs.CounselorDTO;
import com.example.seniorProject.models.DTOs.RegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class CounselorMapper {

    public Counselor RegistrationRequestToCounselor(RegistrationRequest registrationRequest) {
        Counselor counselor = new Counselor();
        counselor.setFirstName(registrationRequest.getFirstName());
        counselor.setLastName(registrationRequest.getLastName());
        counselor.setUsername(registrationRequest.getUsername());
        counselor.setPassword(registrationRequest.getPassword());
        return counselor;
    }

    public CounselorDTO CounselorToDTO(Counselor counselor) {
        CounselorDTO counselorDTO = new CounselorDTO();
        counselorDTO.setCounselorId(counselor.getId());
        counselorDTO.setFirstName(counselor.getFirstName());
        counselorDTO.setLastName(counselor.getLastName());
        counselorDTO.setUsername(counselor.getUsername());
        counselorDTO.setSessions(counselor.getSessions());
        return counselorDTO;
    }
}
