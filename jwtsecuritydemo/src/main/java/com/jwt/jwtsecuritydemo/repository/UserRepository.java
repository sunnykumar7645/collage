package com.jwt.jwtsecuritydemo.repository;

import java.util.Optional;

import com.jwt.jwtsecuritydemo.model.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.jwtsecuritydemo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
   Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}
