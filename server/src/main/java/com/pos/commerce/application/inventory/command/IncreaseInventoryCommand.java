package com.pos.commerce.application.inventory.command;

public record IncreaseInventoryCommand(
        Long productId,
        Integer quantity,
        String description,
        String createdBy
) {
}





