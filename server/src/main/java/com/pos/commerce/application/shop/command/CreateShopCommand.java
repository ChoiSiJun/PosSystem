package com.pos.commerce.application.shop.command;

public record CreateShopCommand(
        String shopCode,
        String shopName,
        String password,
        String adminPassword
) {
}





