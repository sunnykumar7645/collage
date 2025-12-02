package com.springsecurity.jwtsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. Temporarily disable CSRF entirely for all paths
            .csrf(csrf -> csrf.disable())
            
            // 2. CRITICAL: Disable X-Frame-Options header
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable)) 
            
            // 3. Temporarily allow ALL requests (for testing)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
    
}
