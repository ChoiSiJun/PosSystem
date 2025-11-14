package com.pos.commerce.presentation.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

import com.pos.commerce.domain.user.Role;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;
    private Boolean enabled;
}




