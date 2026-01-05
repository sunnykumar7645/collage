package com.jwt.jwtsecuritydemo.DTO;

import com.jwt.jwtsecuritydemo.model.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {
    private String  username;
    private String password;
    private String name;
    private Set<RoleType> roles = new HashSet<>();
}
