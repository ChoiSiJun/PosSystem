package com.pos.commerce.application.inventory.command;

public record DecreaseInventoryCommand(
        Long productId,
        Integer quantity,
        String description,
        String createdBy
) {
}





