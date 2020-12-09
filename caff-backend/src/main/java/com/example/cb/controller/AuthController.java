package com.example.cb.controller;

import com.example.cb.payload.LoginRequest;
import com.example.cb.payload.SignupRequest;
import com.example.cb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.loginUser(loginRequest);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

}
