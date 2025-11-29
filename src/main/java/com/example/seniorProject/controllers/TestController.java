package com.example.seniorProject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test(){
        LocalDateTime now = LocalDateTime.now();
        now = now.minusHours(1);


        return "test";
    }
}
