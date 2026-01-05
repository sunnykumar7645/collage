package com.jwt.jwtsecuritydemo.service;

import com.jwt.jwtsecuritydemo.model.type.RoleType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

// import com.jwt.jwtsecuritydemo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserService {

    @PreAuthorize("hasRole('ADMIN')")
    public String getWelcome(){
        return "wel come by uesr service";
    }

    






}
