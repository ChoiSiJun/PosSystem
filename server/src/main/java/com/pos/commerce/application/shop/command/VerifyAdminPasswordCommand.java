package com.pos.commerce.application.shop.command;

public record VerifyAdminPasswordCommand(
    String shopCode,
    String password
) {
}

