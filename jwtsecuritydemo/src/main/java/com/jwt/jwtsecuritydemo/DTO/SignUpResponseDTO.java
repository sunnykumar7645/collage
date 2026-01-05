package com.jwt.jwtsecuritydemo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDTO {
    private Long id;
    private String username;
    private String message;
}
