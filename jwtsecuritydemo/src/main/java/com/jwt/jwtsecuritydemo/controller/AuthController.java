package com.jwt.jwtsecuritydemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.jwtsecuritydemo.DTO.LoginRequestDTO;
import com.jwt.jwtsecuritydemo.DTO.LoginResponseDTO;
import com.jwt.jwtsecuritydemo.DTO.SignUpResponseDTO;
import com.jwt.jwtsecuritydemo.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO LoginRequestDTO) {
        LoginResponseDTO response = authService.login(LoginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody LoginRequestDTO signupRequestDTO) {
        SignUpResponseDTO response = authService.signup(signupRequestDTO);
        return ResponseEntity.ok(response);
    }
}
