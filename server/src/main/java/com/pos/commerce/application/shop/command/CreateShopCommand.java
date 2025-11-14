package com.pos.commerce.application.shop.command;

public record CreateShopCommand(
        String name,
        String description,
        String address,
        Long ownerId
) {
}





