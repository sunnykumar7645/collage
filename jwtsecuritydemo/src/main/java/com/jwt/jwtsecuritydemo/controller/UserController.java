package com.jwt.jwtsecuritydemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
public class UserController {


    @GetMapping
    public String welcome() {
        return "Welcome to JWT Security Demo";
    }
    
    
}
