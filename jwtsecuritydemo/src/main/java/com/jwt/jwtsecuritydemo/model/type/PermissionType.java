package com.jwt.jwtsecuritydemo.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    APPOINTMENT_READ("appointment:read"),
    APPOINTMENT_WRITE("appointment:write"),
    APPOINTMENT_DELETE("appointment:delete"),
    USER_MANAGE("user:manage"), // For admin tasks
    REPORT_VIEW("report:view");

    private final String permission;
}