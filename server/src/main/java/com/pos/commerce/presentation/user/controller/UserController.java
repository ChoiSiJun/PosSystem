package com.pos.commerce.presentation.user.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.user.UserService;
import com.pos.commerce.application.user.command.DeleteUserCommand;
import com.pos.commerce.application.user.command.UpdateUserCommand;
import com.pos.commerce.application.user.command.RegisterUserCommand;
import com.pos.commerce.application.user.query.GetUserByIdQuery;
import com.pos.commerce.domain.user.Role;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.user.dto.UserRequest;
import com.pos.commerce.presentation.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        Set<Role> roles = request.getRoles() != null && !request.getRoles().isEmpty()
                ? request.getRoles()
                : Set.of(Role.USER);

        RegisterUserCommand command = new RegisterUserCommand(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                roles,
                request.getEnabled()
        );

        UserResponse created = UserResponse.from(userService.createUser(command));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("사용자가 등록 완료되었습니다.", created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return userService.getUserById(new GetUserByIdQuery(id))
                .map(user -> ResponseEntity.ok(ApiResponse.success(UserResponse.from(user))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {
        UpdateUserCommand command = new UpdateUserCommand(
                id,
                request.getPassword(),
                request.getEmail(),
                request.getRoles(),
                request.getEnabled()
        );

        UserResponse updated = UserResponse.from(userService.updateUser(command));
        return ResponseEntity.ok(ApiResponse.success("사용자가 수정되었습니다.", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(new DeleteUserCommand(id));
        return ResponseEntity.ok(ApiResponse.success("사용자가 삭제되었습니다.", null));
    }
}

