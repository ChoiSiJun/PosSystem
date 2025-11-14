package com.pos.commerce.presentation.user.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.user.UserService;
import com.pos.commerce.application.user.command.LoginUserCommand;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.user.dto.LoginRequest;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /* @로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {

        String token = userService.authenticate(new LoginUserCommand(request.getUsername(), request.getPassword()));

        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", token));
    }               
}


