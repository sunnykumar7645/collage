package com.springsecurity.jwtsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    
     String generateToken(UserDetails userDetails);
     String extractUsername(String token);
     boolean isTokenValid(String token, UserDetails userDetails);
}
