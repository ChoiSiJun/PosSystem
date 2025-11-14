package com.pos.commerce.application.user.command;

public record LoginUserCommand(
        String username,
        String password
) {
}
