package com.example.seniorProject.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String text;

    private LocalDateTime date;

    // true if sender is counselor, false if sender is student
    private boolean sender;

    @JsonIgnoreProperties({"sessions", "messages", "password", "role", "authorities", "enabled", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired"})
    @ManyToOne
    @JoinColumn(name = "counselorId")
    private Counselor counselor;

    @JsonIgnoreProperties({"sessions", "messages", "password", "role", "authorities", "enabled", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired"})
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;


}
