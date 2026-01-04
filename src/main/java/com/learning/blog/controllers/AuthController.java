package com.learning.blog.controllers;

import com.learning.blog.domain.dtos.AuthResponse;
import com.learning.blog.domain.dtos.LoginRequest;
import com.learning.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = authenticationService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(authResponse);
    }
}
