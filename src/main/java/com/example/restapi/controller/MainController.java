package com.example.restapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("g")
    public ResponseEntity<String> g(){
        return ResponseEntity.ok("Hello!");
    }

    @GetMapping
    public ResponseEntity<String> gg(){
        return ResponseEntity.ok("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA!");
    }
}
