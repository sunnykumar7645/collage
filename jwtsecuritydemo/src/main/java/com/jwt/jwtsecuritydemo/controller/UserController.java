package com.jwt.jwtsecuritydemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class UserController {


    @GetMapping("/user")
    public String welcome() {
        return "Welcome to JWT Security Demo";
    }
    
    
}
