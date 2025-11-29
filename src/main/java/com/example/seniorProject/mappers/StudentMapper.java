package com.example.seniorProject.mappers;

import com.example.seniorProject.models.DTOs.RegistrationRequest;
import com.example.seniorProject.models.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student RegistrationRequestToStudent(RegistrationRequest registrationRequest) {
        Student student = new Student();
        student.setFirstName(registrationRequest.getFirstName());
        student.setLastName(registrationRequest.getLastName());
        student.setUsername(registrationRequest.getUsername());
        student.setPassword(registrationRequest.getPassword());
        return student;
    }
}
