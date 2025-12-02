package com.springsecurity.jwtsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springsecurity.jwtsecurity.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // User findByUsername(String username);
    Optional<User> findByEmail(String Email);
    
}
