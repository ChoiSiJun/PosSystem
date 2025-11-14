package com.pos.commerce.application.user.command;

import java.util.Set;

import com.pos.commerce.domain.user.Role;

public record RegisterUserCommand(
        String username,
        String password,
        String email,
        Set<Role> roles,
        Boolean enabled
) {
}
