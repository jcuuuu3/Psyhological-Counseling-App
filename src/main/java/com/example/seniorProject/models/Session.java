package com.example.seniorProject.models;

import com.example.seniorProject.models.enums.SessionStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonIgnoreProperties({"sessions", "messages", "password", "role", "authorities", "enabled", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired"})
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @JsonIgnoreProperties({"sessions", "messages", "password", "role", "authorities", "enabled", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired"})
    @ManyToOne
    @JoinColumn(name = "counselorId")
    private Counselor counselor;

    private LocalDateTime startTime;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;
}
