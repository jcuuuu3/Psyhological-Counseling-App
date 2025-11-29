package com.example.seniorProject.models.DTOs;

import com.example.seniorProject.models.Session;
import lombok.Data;

import java.util.List;

@Data
public class CounselorDTO {

    private int counselorId;
    private String firstName;
    private String lastName;
    private String username;
    private List<Session> sessions;
}
