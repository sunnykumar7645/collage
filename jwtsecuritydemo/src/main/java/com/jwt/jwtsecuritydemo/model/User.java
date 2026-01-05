package com.jwt.jwtsecuritydemo.model;



import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jwt.jwtsecuritydemo.model.type.AuthProviderType;
import com.jwt.jwtsecuritydemo.model.type.RoleType;
import com.jwt.jwtsecuritydemo.security.RolePermissionMapping;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Slf4j
@Entity
@Table(name = "user-data", indexes={
        @Index(name = "idx_provider_id_provider_type", columnList = "providerId, providerType")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    
    private String password;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<RoleType> roles = new HashSet<>(); 


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        here we are only defining role without authority
//        return roles.stream()
//                .map(role-> new SimpleGrantedAuthority("ROLE_" + role.name()))
//                .collect(Collectors.toSet());

//        Here we defing role with authority that which level of role what action can be performed.
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                role -> {
                    Set<SimpleGrantedAuthority> permissions = RolePermissionMapping.getAuthoritiesForRole(role);
                    log.info(String.valueOf(role));
                    authorities.addAll(permissions);
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
                }
        );
//        log.info("String data : {}" + authorities);
        return authorities;
        
    }
    
}
