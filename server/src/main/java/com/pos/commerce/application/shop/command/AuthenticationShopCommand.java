package com.pos.commerce.application.shop.command;

public record AuthenticationShopCommand(
        String shopCode,
        String password
) {
}