package com.jwt.jwtsecuritydemo.security;

import com.jwt.jwtsecuritydemo.model.type.PermissionType;
import com.jwt.jwtsecuritydemo.model.type.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jwt.jwtsecuritydemo.model.type.PermissionType.*;
import static com.jwt.jwtsecuritydemo.model.type.RoleType.*;


@Slf4j
public class RolePermissionMapping {

    private static final Map<RoleType, Set<PermissionType>> map = Map.of(
            USER, Set.of(USER_READ, APPOINTMENT_READ, APPOINTMENT_WRITE),
            OPS, Set.of(APPOINTMENT_DELETE, APPOINTMENT_WRITE, APPOINTMENT_READ, USER_READ),
            ADMIN, Set.of(USER_READ, USER_WRITE, APPOINTMENT_READ, APPOINTMENT_WRITE, APPOINTMENT_DELETE, USER_MANAGE, REPORT_VIEW),
            SUPERADMIN, Set.of(USER_READ, USER_WRITE, APPOINTMENT_READ, APPOINTMENT_WRITE, APPOINTMENT_DELETE, USER_MANAGE, REPORT_VIEW)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(RoleType role) {
//        Set<SimpleGrantedAuthority> authorities = map.get(role).stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toSet());
//
//        log.info("Authorities for role {}: {}", role, authorities);
//
//        return authorities;
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}