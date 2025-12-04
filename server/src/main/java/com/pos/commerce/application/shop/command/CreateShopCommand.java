package com.pos.commerce.application.shop.command;

public record CreateShopCommand(
        String shopCode,
        String password,
        String adminPassword
) {
}





