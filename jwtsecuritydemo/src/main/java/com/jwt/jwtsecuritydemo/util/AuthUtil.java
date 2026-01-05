package com.jwt.jwtsecuritydemo.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import com.jwt.jwtsecuritydemo.model.type.AuthProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.jwt.jwtsecuritydemo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Component
public class AuthUtil {

    @Value("${jwt.jwtSecret}")
    private String  jwtSecret;
    

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(secretKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
     return extractAllClaims(token).getSubject(); // 👈 username
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId){

        return switch (registrationId.toLowerCase()){
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            case "facebook" -> AuthProviderType.FACEBOOK;
            default -> throw new IllegalArgumentException("Unsupported OAuth2Provider : " + registrationId);
        };

    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId){
        String providerType = switch (registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();
            default -> {
                log.error("Unsupported OAuth2 provider: " + registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 provider: {}" + registrationId);
            }
        };
        if(providerType == null || providerType.isBlank()){
            log.error("Unable to determine providerId for provider: " + registrationId);
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");

        }
        return providerType;
    }

    public String determineUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerType){
        String email = oAuth2User.getAttribute("email");
        if(email != null && !email.isBlank()){
            return email;
        }
        return switch (registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerType;
        };
    }










}
