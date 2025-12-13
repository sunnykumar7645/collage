package com.jwt.jwtsecuritydemo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.jwtsecuritydemo.DTO.LoginRequestDTO;
import com.jwt.jwtsecuritydemo.DTO.LoginResponseDTO;
import com.jwt.jwtsecuritydemo.DTO.SignUpResponseDTO;
import com.jwt.jwtsecuritydemo.model.User;
import com.jwt.jwtsecuritydemo.repository.UserRepository;
import com.jwt.jwtsecuritydemo.util.AuthUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        
        Authentication authentication = authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword() 
            )
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateToken(user);
        return new LoginResponseDTO(token, user.getId());
        
    }

    public SignUpResponseDTO signup(LoginRequestDTO signupRequestDTO) {
        if(userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }   

        User user = userRepository.save(User.builder()
            .username(signupRequestDTO.getUsername())
            .password( passwordEncoder.encode(signupRequestDTO.getPassword()))
            .build()
        );
        return new SignUpResponseDTO(user.getId(), authUtil.generateToken(user));
    }
    
}
